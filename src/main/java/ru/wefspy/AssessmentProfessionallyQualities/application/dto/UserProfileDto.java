package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.Education;

import java.util.Collection;

public record UserProfileDto(
        Long id,
        String username,
        String email,
        String firstName,
        String middleName,
        String lastName,
        Short courseNumber,
        Education education,
        Collection<RoleDto> roles
) {
}
