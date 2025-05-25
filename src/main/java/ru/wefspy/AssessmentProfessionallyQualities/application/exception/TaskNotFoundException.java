package ru.wefspy.AssessmentProfessionallyQualities.application.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
} 