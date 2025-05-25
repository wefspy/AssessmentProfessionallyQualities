package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.*;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.SkillCategoryNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.*;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.*;

import java.util.*;

@Service
public class UserService {
    private final JdbcUserRepository userRepository;
    private final JdbcUserInfoRepository userInfoRepository;
    private final JdbcSkillCategoryRepository skillCategoryRepository;
    private final JdbcSkillRepository skillRepository;
    private final JdbcUserSkillRepository userSkillRepository;
    private final JdbcTeamRepository teamRepository;
    private final JdbcTeamMemberRepository teamMemberRepository;

    public UserService(JdbcUserRepository userRepository,
                       JdbcUserInfoRepository userInfoRepository,
                       JdbcSkillCategoryRepository skillCategoryRepository,
                       JdbcSkillRepository skillRepository,
                       JdbcUserSkillRepository userSkillRepository,
                       JdbcTeamRepository teamRepository,
                       JdbcTeamMemberRepository teamMemberRepository) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.skillCategoryRepository = skillCategoryRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public void setMainSkillCategory(Long userId, SetMainSkillCategoryRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        skillCategoryRepository.findById(request.skillCategoryId())
                .orElseThrow(() -> new SkillCategoryNotFoundException(
                        String.format("Skill category with id %d not found", request.skillCategoryId())
                ));

        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User info for user with id %d not found", userId)
                ));

        userInfo.setMainSkillCategoryId(request.skillCategoryId());
        userInfoRepository.update(userInfo);
    }

    public void removeMainSkillCategory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User info for user with id %d not found", userId)
                ));

        userInfo.setMainSkillCategoryId(null);
        userInfoRepository.update(userInfo);
    }

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id %d не найден", userId)
                ));

        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Информация о пользователе с id %d не найдена", userId)
                ));

        SkillCategoryDto mainSkillCategory = null;
        if (userInfo.getMainSkillCategoryId() != null) {
            SkillCategory category = skillCategoryRepository.findById(userInfo.getMainSkillCategoryId())
                    .orElse(null);
            if (category != null) {
                mainSkillCategory = new SkillCategoryDto(category.getId(), category.getName());
            }
        }

        List<UserSkill> userSkills = userSkillRepository.findAllByUserId(userId);
        SkillDto bestSkill = null;
        Collection<SkillCategoryDto> skillCategories = new ArrayList<>();

        if (!userSkills.isEmpty()) {
            UserSkill bestUserSkill = userSkills.stream()
                    .max(Comparator.comparing(UserSkill::getRating))
                    .orElse(null);

            Skill skill = skillRepository.findById(bestUserSkill.getSkillId()).orElse(null);
            if (skill != null) {
                bestSkill = new SkillDto(
                        skill.getId(),
                        skill.getName(),
                        bestUserSkill.getRating()
                );

                Set<Long> processedCategoryIds = new HashSet<>();
                for (UserSkill userSkill : userSkills) {
                    Skill userSkillObj = skillRepository.findById(userSkill.getSkillId()).orElse(null);
                    if (userSkillObj != null && !processedCategoryIds.contains(userSkillObj.getSkillCategoryId())) {
                        SkillCategory category = skillCategoryRepository.findById(userSkillObj.getSkillCategoryId()).orElse(null);
                        if (category != null) {
                            skillCategories.add(new SkillCategoryDto(category.getId(), category.getName()));
                            processedCategoryIds.add(category.getId());
                        }
                    }
                }
            }
        }

        Integer totalNumberSkills = userSkills.size();

        List<TeamMember> teamMembers = teamMemberRepository.findAllByUserId(userId);
        Collection<TeamMemberDto> teams = new ArrayList<>();
        for (TeamMember member : teamMembers) {
            teamRepository.findById(member.getTeamId())
                    .ifPresent(team -> teams.add(
                            new TeamMemberDto(
                                    team.getId(),
                                    team.getName(),
                                    member.getRole()
                            )));
        }

        return new UserProfileDto(
                userInfo.getFirstName(),
                userInfo.getMiddleName(),
                userInfo.getLastName(),
                mainSkillCategory,
                bestSkill,
                totalNumberSkills,
                teams,
                skillCategories
        );
    }

    public Collection<UserSkillCategoryDto> getUserSkillCategories(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id %d не найден", userId)
                ));

        List<UserSkill> userSkills = userSkillRepository.findAllByUserId(userId);
        Map<Long, List<UserSkill>> skillsByCategoryId = new HashMap<>();
        Map<Long, SkillCategory> categoriesById = new HashMap<>();

        for (UserSkill userSkill : userSkills) {
            Skill skill = skillRepository.findById(userSkill.getSkillId()).orElse(null);
            if (skill != null) {
                Long categoryId = skill.getSkillCategoryId();
                SkillCategory category = skillCategoryRepository.findById(categoryId).orElse(null);

                if (category != null) {
                    skillsByCategoryId.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(userSkill);
                    categoriesById.putIfAbsent(categoryId, category);
                }
            }
        }

        Collection<UserSkillCategoryDto> result = new ArrayList<>();
        for (Map.Entry<Long, List<UserSkill>> entry : skillsByCategoryId.entrySet()) {
            Long categoryId = entry.getKey();
            List<UserSkill> categoryUserSkills = entry.getValue();
            SkillCategory category = categoriesById.get(categoryId);

            double totalRating = categoryUserSkills.stream()
                    .mapToDouble(UserSkill::getRating)
                    .sum();
            double averageRating = categoryUserSkills.isEmpty() ? 0.0 : totalRating / categoryUserSkills.size();

            Collection<SkillDto> skillDtos = new ArrayList<>();
            for (UserSkill userSkill : categoryUserSkills) {
                skillRepository.findById(userSkill.getSkillId())
                        .ifPresent(skill -> skillDtos.add(
                                new SkillDto(
                                        skill.getId(),
                                        skill.getName(),
                                        userSkill.getRating()
                                )));
            }

            result.add(new UserSkillCategoryDto(
                    new SkillCategoryDto(category.getId(), category.getName()),
                    skillDtos,
                    averageRating
            ));
        }

        return result;
    }

    public ReviewSkillCategoriesDto getReviewSkillCategories(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id %d не найден", userId)
                ));

        List<UserSkill> userSkills = userSkillRepository.findAllByUserId(userId);
        Map<Long, List<UserSkill>> skillsByCategoryId = new HashMap<>();
        Map<Long, SkillCategory> categoriesById = new HashMap<>();

        for (UserSkill userSkill : userSkills) {
            Skill skill = skillRepository.findById(userSkill.getSkillId()).orElse(null);
            if (skill != null) {
                Long categoryId = skill.getSkillCategoryId();
                SkillCategory category = skillCategoryRepository.findById(categoryId).orElse(null);

                if (category != null) {
                    skillsByCategoryId.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(userSkill);
                    categoriesById.putIfAbsent(categoryId, category);
                }
            }
        }

        List<ReviewSkillCategoryDto> categories = new ArrayList<>();
        for (Map.Entry<Long, List<UserSkill>> entry : skillsByCategoryId.entrySet()) {
            Long categoryId = entry.getKey();
            List<UserSkill> categoryUserSkills = entry.getValue();
            SkillCategory category = categoriesById.get(categoryId);

            double totalRating = categoryUserSkills.stream()
                    .mapToDouble(UserSkill::getRating)
                    .sum();
            double averageRating = categoryUserSkills.isEmpty() ? 0.0 : totalRating / categoryUserSkills.size();

            categories.add(new ReviewSkillCategoryDto(
                    category.getId(),
                    category.getName(),
                    averageRating
            ));
        }

        return new ReviewSkillCategoriesDto(categories);
    }
} 