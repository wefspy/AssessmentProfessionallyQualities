package ru.wefspy.AssessmentProfessionallyQualities.application.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
