package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TeamMemberStatsDTO;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcTeamMemberStatsRepository;

import java.util.List;

@Service
public class TeamMemberStatsService {
    private final JdbcTeamMemberStatsRepository teamMemberStatsRepository;

    public TeamMemberStatsService(JdbcTeamMemberStatsRepository teamMemberStatsRepository) {
        this.teamMemberStatsRepository = teamMemberStatsRepository;
    }

    public List<TeamMemberStatsDTO> getTeamMemberStats(Long teamId) {
        return teamMemberStatsRepository.findTeamMemberStatsByTeamId(teamId);
    }
} 