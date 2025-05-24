package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.exception;

public class ClaimNotFoundException extends RuntimeException {
    public ClaimNotFoundException(String message) {
        super(message);
    }
}
