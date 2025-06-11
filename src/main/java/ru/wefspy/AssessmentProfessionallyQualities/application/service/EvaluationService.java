package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.EvaluationRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserSkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.SkillCategoryNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.TaskNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.*;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EvaluationService {
    private final JdbcTaskRepository taskRepository;
    private final JdbcTeamMemberRepository teamMemberRepository;
    private final JdbcUserSkillRepository userSkillRepository;
    private final JdbcSkillRepository skillRepository;
    private final JdbcEvaluationRepository evaluationRepository;
    private final JdbcUserRepository userRepository;
    private final JdbcSkillCategoryRepository skillCategoryRepository;
    private final JdbcTaskEvaluatedSkillsRepository taskEvaluatedSkillsRepository;

    public EvaluationService(
            JdbcTaskRepository taskRepository,
            JdbcTeamMemberRepository teamMemberRepository,
            JdbcUserSkillRepository userSkillRepository,
            JdbcSkillRepository skillRepository,
            JdbcEvaluationRepository evaluationRepository,
            JdbcUserRepository userRepository,
            JdbcSkillCategoryRepository skillCategoryRepository,
            JdbcTaskEvaluatedSkillsRepository taskEvaluatedSkillsRepository) {
        this.taskRepository = taskRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userSkillRepository = userSkillRepository;
        this.skillRepository = skillRepository;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.skillCategoryRepository = skillCategoryRepository;
        this.taskEvaluatedSkillsRepository = taskEvaluatedSkillsRepository;
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

        // Get the list of user skills that should be evaluated for this task
        List<Long> taskSkillIds = taskEvaluatedSkillsRepository.findAllByTaskId(taskId);
        
        // Get all user skills for the assignee that match the task's required skills
        List<UserSkill> assigneeSkills = userSkillRepository.findAllByUserIdAndSkillIds(assignee.getUserId(), taskSkillIds);
        Map<Long, UserSkill> userSkillsMap = assigneeSkills.stream()
                .collect(Collectors.toMap(
                    UserSkill::getSkillId,
                    us -> us
                ));
        
        Collection<Evaluation> evaluations = new ArrayList<>();
        for (EvaluationRequest.SkillEvaluation skillEval : request.skillEvaluations()) {
            UserSkill userSkill = userSkillsMap.get(skillEval.skillId());
            if (userSkill == null) {
                throw new IllegalStateException("Skill " + skillEval.skillId() + " is not in the list of skills to be evaluated for this task's assignee");
            }

            // Create evaluation record
            Evaluation evaluation = new Evaluation(
                taskId,
                userSkill.getId(),
                skillEval.rating(),
                skillEval.feedback()
            );
            evaluations.add(evaluation);

            // Recalculate and update user skill rating
            List<Evaluation> allSkillEvaluations = evaluationRepository.findAllByUserSkillId(userSkill.getId());
            allSkillEvaluations.add(evaluation); // Add current evaluation
            
            // Calculate average rating
            double totalRating = allSkillEvaluations.stream()
                    .mapToInt(e -> e.getEvaluation())
                    .sum();
            short newRating = (short) Math.round(totalRating / allSkillEvaluations.size());
            
            // Update user skill rating
            userSkill.setRating(newRating);
            userSkillRepository.update(userSkill);
        }

        // Save all evaluations
        evaluationRepository.saveAll(evaluations);

        // Update task status to RATED
        taskRepository.updateStatus(taskId, TaskStatus.RATED);
    }

    public Collection<SkillDto> getSkillsForEvaluation(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        // Get the list of user skills that should be evaluated for this task
        List<Long> taskUserSkillIds = taskEvaluatedSkillsRepository.findAllByTaskId(taskId);
        
        // Get all user skills that are to be evaluated
        List<UserSkill> userSkills = userSkillRepository.findAllByIds(taskUserSkillIds);

        return userSkills.stream()
                .map(userSkill -> {
                    Skill skill = skillRepository.findById(userSkill.getSkillId())
                            .orElseThrow(() -> new IllegalStateException("Skill not found"));
                    
                    return new SkillDto(
                        skill.getId(),
                        skill.getName(),
                        userSkill.getRating(),
                        skill.getIsNecessary()
                    );
                })
                .collect(Collectors.toList());
    }

    public UserSkillCategoryDto getUserSoftSkills(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id %d не найден", userId)
                ));

        // Get the Soft Skills category
        SkillCategory softSkillsCategory = skillCategoryRepository.findByName("Soft Skills")
                .orElseThrow(() -> new SkillCategoryNotFoundException("Soft Skills category not found"));

        // Get all user's soft skills
        List<UserSkill> userSkills = userSkillRepository.findAllByUserId(userId);
        List<UserSkill> softSkills = new ArrayList<>();
        Collection<SkillDto> skillDtos = new ArrayList<>();

        for (UserSkill userSkill : userSkills) {
            Skill skill = skillRepository.findById(userSkill.getSkillId()).orElse(null);
            if (skill != null && skill.getSkillCategoryId().equals(softSkillsCategory.getId())) {
                softSkills.add(userSkill);
                skillDtos.add(new SkillDto(
                        skill.getId(),
                        skill.getName(),
                        userSkill.getRating(),
                        skill.getIsNecessary()
                ));
            }
        }

        // Calculate average rating
        double totalRating = softSkills.stream()
                .mapToDouble(UserSkill::getRating)
                .sum();
        double averageRating = softSkills.isEmpty() ? 0.0 : (totalRating / softSkills.size()) / 2;

        return new UserSkillCategoryDto(
                new SkillCategoryDto(softSkillsCategory.getId(), softSkillsCategory.getName(), softSkillsCategory.getColor()),
                skillDtos,
                averageRating
        );
    }
} 