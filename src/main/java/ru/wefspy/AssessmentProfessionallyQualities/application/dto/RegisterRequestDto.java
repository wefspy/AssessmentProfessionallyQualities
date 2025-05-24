package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Username обязательное поле")
        String username,

        @Email(message = "Email не валидный")
        String email,

        @NotBlank(message = "Имя обязательное поле")
        String firstName,

        String middleName,

        @NotBlank(message = "Фамилия обязательное поле")
        String lastName,

        @NotBlank(message = "Пароль обязательное поле")
        @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
        String password
) {
}
