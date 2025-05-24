package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillCategoryDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.SkillDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TeamMemberDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserProfileDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.*;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
} 