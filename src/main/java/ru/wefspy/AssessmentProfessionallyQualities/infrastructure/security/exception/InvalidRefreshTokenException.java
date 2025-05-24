package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
} 