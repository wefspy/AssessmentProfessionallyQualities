package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

public record UserTeamsDto(
        Long teamId,
        String teamName,
        Long skillCategoryId,
        String skillCategoryName
) {
} 