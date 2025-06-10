package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Collection;

public record CreateTasksRequest(
        @NotEmpty(message = "Tasks collection cannot be empty")
        @Size(max = 100, message = "Cannot create more than 100 tasks at once")
        Collection<@Valid CreateTaskRequest> tasks
) {} 