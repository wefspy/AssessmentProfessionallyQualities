package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.NotBlank;

public record SkillSearchRequest(
        @NotBlank(message = "Запрос не может быть пустой")
        String query
) {
} 