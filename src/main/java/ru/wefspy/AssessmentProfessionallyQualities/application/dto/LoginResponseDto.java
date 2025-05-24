package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {
}
