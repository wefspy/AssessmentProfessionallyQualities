package ru.wefspy.AssessmentProfessionallyQualities.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TeamMemberStatsDTO;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.TeamMemberStatsService;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamMemberStatsController {
    private final TeamMemberStatsService teamMemberStatsService;

    public TeamMemberStatsController(TeamMemberStatsService teamMemberStatsService) {
        this.teamMemberStatsService = teamMemberStatsService;
    }

    @GetMapping("/{teamId}/member-stats")
    public ResponseEntity<List<TeamMemberStatsDTO>> getTeamMemberStats(@PathVariable Long teamId) {
        List<TeamMemberStatsDTO> stats = teamMemberStatsService.getTeamMemberStats(teamId);
        return ResponseEntity.ok(stats);
    }
} 