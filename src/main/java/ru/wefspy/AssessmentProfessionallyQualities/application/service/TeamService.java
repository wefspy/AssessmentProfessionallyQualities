package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserTeamsDto;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Team;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.TeamMember;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcSkillCategoryRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcTeamMemberRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcTeamRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final JdbcTeamRepository teamRepository;
    private final JdbcTeamMemberRepository teamMemberRepository;
    private final JdbcSkillCategoryRepository skillCategoryRepository;

    public TeamService(JdbcTeamRepository teamRepository,
                      JdbcTeamMemberRepository teamMemberRepository,
                      JdbcSkillCategoryRepository skillCategoryRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.skillCategoryRepository = skillCategoryRepository;
    }

    public Collection<UserTeamsDto> getUserTeams(Long userId) {
        // Get all team memberships for the user
        List<TeamMember> teamMembers = teamMemberRepository.findAllByUserId(userId);
        
        if (teamMembers.isEmpty()) {
            return new ArrayList<>();
        }

        // Get all teams for these memberships
        Map<Long, Team> teamsMap = teamRepository.findAllByIds(
                teamMembers.stream()
                        .map(TeamMember::getTeamId)
                        .collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(Team::getId, team -> team));

        // Get all skill categories for these memberships
        Map<Long, SkillCategory> skillCategoriesMap = skillCategoryRepository.findAllByIds(
                teamMembers.stream()
                        .map(TeamMember::getSkillCategoryId)
                        .collect(Collectors.toList())
        ).stream()
                .collect(Collectors.toMap(SkillCategory::getId, category -> category));

        // Create DTOs
        return teamMembers.stream()
                .map(member -> {
                    Team team = teamsMap.get(member.getTeamId());
                    SkillCategory category = skillCategoriesMap.get(member.getSkillCategoryId());
                    
                    return new UserTeamsDto(
                            team.getId(),
                            team.getName(),
                            category.getId(),
                            category.getName(),
                            category.getColor()
                    );
                })
                .collect(Collectors.toList());
    }
} 