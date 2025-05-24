package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.dto;

import java.util.Set;

public record AccessTokenInput(
        Long userId,
        String username,
        Set<String> roles
) {
}
