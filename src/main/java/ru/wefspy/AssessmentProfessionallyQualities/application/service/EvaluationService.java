package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.EvaluationRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.TaskNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.*;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvaluationService {
    private final JdbcTaskRepository taskRepository;
    private final JdbcTeamMemberRepository teamMemberRepository;
    private final JdbcUserSkillRepository userSkillRepository;
    private final JdbcSkillRepository skillRepository;
    private final JdbcEvaluationRepository evaluationRepository;

    public EvaluationService(
            JdbcTaskRepository taskRepository,
            JdbcTeamMemberRepository teamMemberRepository,
            JdbcUserSkillRepository userSkillRepository,
            JdbcSkillRepository skillRepository,
            JdbcEvaluationRepository evaluationRepository) {
        this.taskRepository = taskRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userSkillRepository = userSkillRepository;
        this.skillRepository = skillRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Transactional
    public void evaluateTask(Long taskId, EvaluationRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (task.getStatus() != TaskStatus.IN_WAITING) {
            throw new IllegalStateException("Task is not in IN_WAITING status");
        }

        TeamMember assignee = teamMemberRepository.findById(task.getAssigneeMemberId())
                .orElseThrow(() -> new IllegalStateException("Assignee team member not found"));

        List<UserSkill> userSkills = userSkillRepository.findAllByUserId(assignee.getUserId());
        
        Collection<Evaluation> evaluations = new ArrayList<>();
        for (EvaluationRequest.SkillEvaluation skillEval : request.skillEvaluations()) {
            UserSkill userSkill = userSkills.stream()
                    .filter(us -> us.getSkillId().equals(skillEval.skillId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("User does not have the skill being evaluated"));

            // Create evaluation record
            Evaluation evaluation = new Evaluation(
                taskId,
                userSkill.getId(),
                skillEval.rating(),
                skillEval.feedback()
            );
            evaluations.add(evaluation);

            // Get all evaluations for this user skill
            List<Evaluation> allEvaluations = evaluationRepository.findAllByUserSkillId(userSkill.getId());
            allEvaluations.add(evaluation); // Add the new evaluation

            // Calculate average rating from all evaluations
            double averageRating = allEvaluations.stream()
                    .mapToInt(e -> e.getEvaluation())
                    .average()
                    .orElse(skillEval.rating());

            // Update user skill rating with the average
            userSkill.setRating((short) Math.round(averageRating));
            userSkillRepository.update(userSkill);
        }

        // Save all evaluations
        evaluationRepository.saveAll(evaluations);

        // Update task status
        task.setStatus(TaskStatus.RATED);
        taskRepository.update(task);
    }

    public Collection<SkillDto> getSkillsForEvaluation(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        TeamMember assignee = teamMemberRepository.findById(task.getAssigneeMemberId())
                .orElseThrow(() -> new IllegalStateException("Assignee team member not found"));

        List<UserSkill> userSkills = userSkillRepository.findAllByUserId(assignee.getUserId());

        return userSkills.stream()
                .map(userSkill -> {
                    Skill skill = skillRepository.findById(userSkill.getSkillId())
                            .orElseThrow(() -> new IllegalStateException("Skill not found"));
                    
                    // Only return skills that belong to the team member's skill category
                    if (skill.getSkillCategoryId().equals(assignee.getSkillCategoryId())) {
                        return new SkillDto(
                            skill.getId(),
                            skill.getName(),
                            userSkill.getRating()
                        );
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
} 