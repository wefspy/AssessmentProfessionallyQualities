package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.exception;

public class InvalidJwtTokenTypeException extends RuntimeException {
    public InvalidJwtTokenTypeException(String message) {
        super(message);
    }
}
