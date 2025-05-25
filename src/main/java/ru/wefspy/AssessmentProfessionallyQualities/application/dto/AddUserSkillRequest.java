package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.NotNull;

public record AddUserSkillRequest(
        @NotNull(message = "Skill ID обязательное поле")
        Long skillId
) {
} 