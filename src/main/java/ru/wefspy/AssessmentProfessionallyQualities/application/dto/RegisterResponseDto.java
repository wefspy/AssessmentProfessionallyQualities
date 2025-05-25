package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.Collection;

public record RegisterResponseDto(
        Long id,
        String username,
        Collection<RoleDto> roles
) {
}
