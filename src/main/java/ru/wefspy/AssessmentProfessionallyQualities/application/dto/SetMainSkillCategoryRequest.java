package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.NotNull;

public record SetMainSkillCategoryRequest(
        @NotNull(message = "Skill category ID обязательное поле")
        Long skillCategoryId
) {
} 