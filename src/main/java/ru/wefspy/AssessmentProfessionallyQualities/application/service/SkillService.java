package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.AddUserSkillRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillSearchRequest;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserSkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.SkillCategoryNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.SkillNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Skill;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserSkill;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SkillService {
    private final JdbcUserRepository userRepository;
    private final JdbcSkillRepository skillRepository;
    private final JdbcUserSkillRepository userSkillRepository;
    private final JdbcEvaluationRepository evaluationRepository;
    private final JdbcSkillCategoryRepository skillCategoryRepository;

    public SkillService(JdbcUserRepository userRepository,
                       JdbcSkillRepository skillRepository,
                       JdbcUserSkillRepository userSkillRepository,
                       JdbcEvaluationRepository evaluationRepository,
                       JdbcSkillCategoryRepository skillCategoryRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
        this.evaluationRepository = evaluationRepository;
        this.skillCategoryRepository = skillCategoryRepository;
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