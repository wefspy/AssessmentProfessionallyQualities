package ru.wefspy.AssessmentProfessionallyQualities.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TeamMemberStatsDTO;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.TeamMemberStatsService;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamMemberStatsController {
    private final TeamMemberStatsService teamMemberStatsService;
    private final JdbcUserRepository userRepository;

    public TeamMemberStatsController(TeamMemberStatsService teamMemberStatsService,
                                   JdbcUserRepository userRepository) {
        this.teamMemberStatsService = teamMemberStatsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{teamId}/member-stats")
    public ResponseEntity<List<TeamMemberStatsDTO>> getTeamMemberStats(
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<TeamMemberStatsDTO> stats = teamMemberStatsService.getTeamMemberStats(teamId, currentUser.getId());
        return ResponseEntity.ok(stats);
    }
} 