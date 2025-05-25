package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

public record TeamMemberInfoDto(
        Long id,
        String firstName,
        String middleName,
        String lastName,
        String role
) {} 