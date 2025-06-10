package ru.wefspy.AssessmentProfessionallyQualities.application.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserTeamsDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.service.TeamService;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;

import java.util.Collection;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/my")
    public Collection<UserTeamsDto> getUserTeams(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return teamService.getUserTeams(userDetails.getId());
    }
} 