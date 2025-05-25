package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.AddUserSkillRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillSearchRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.SkillNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Skill;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserSkill;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcEvaluationRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcSkillRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserSkillRepository;

import java.util.List;

@Service
public class SkillService {
    private final JdbcUserRepository userRepository;
    private final JdbcSkillRepository skillRepository;
    private final JdbcUserSkillRepository userSkillRepository;
    private final JdbcEvaluationRepository evaluationRepository;

    public SkillService(JdbcUserRepository userRepository,
                       JdbcSkillRepository skillRepository,
                       JdbcUserSkillRepository userSkillRepository,
                       JdbcEvaluationRepository evaluationRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public Page<Skill> getAllSkills(Pageable pageable) {
        long total = skillRepository.count();
        List<Skill> skills = skillRepository.findAll(pageable);
        return new PageImpl<>(skills, pageable, total);
    }

    public Page<Skill> searchSkills(SkillSearchRequest request, Pageable pageable) {
        long total = skillRepository.countByNameContainingIgnoreCase(request.query());
        List<Skill> skills = skillRepository.findByNameContainingIgnoreCase(request.query(), pageable);
        return new PageImpl<>(skills, pageable, total);
    }

    public UserSkill addSkillToUser(Long userId, AddUserSkillRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        Skill skill = skillRepository.findById(request.skillId())
                .orElseThrow(() -> new SkillNotFoundException(
                        String.format("Skill with id %d not found", request.skillId())
                ));

        UserSkill userSkill = new UserSkill(userId, skill.getId(), (short) 0);
        return userSkillRepository.save(userSkill);
    }

    public void deleteUserSkill(Long userId, Long skillId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException(
                        String.format("Skill with id %d not found", skillId)
                ));

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId)
                .orElseThrow(() -> new SkillNotFoundException(
                        String.format("Skill with id %d not found for user %d", skillId, userId)
                ));

        boolean hasEvaluations = evaluationRepository.existsByUserSkillId(userSkill.getId());
        if (hasEvaluations) {
            throw new IllegalStateException("Cannot delete skill that has evaluations");
        }

        userSkillRepository.delete(userSkill.getId());
    }
} 