package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Username обязательное поле")
        String username,

        @NotBlank(message = "Пароль не может быть пустыми")
        String password
) {
}
