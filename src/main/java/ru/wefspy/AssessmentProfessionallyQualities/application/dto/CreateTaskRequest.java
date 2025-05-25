package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;

public record CreateTaskRequest(
        @NotNull(message = "Evaluator member ID is required")
        Long evaluatorMemberId,
        
        @NotNull(message = "Assignee member ID is required")
        Long assigneeMemberId,
        
        @NotNull(message = "Lead member ID is required")
        Long leadMemberId,
        
        @NotBlank(message = "Title is required")
        String title,
        
        @NotBlank(message = "Description is required")
        String description,
        
        @NotNull(message = "Deadline completion date is required")
        LocalDate deadlineCompletion,
        
        @NotNull(message = "Status is required")
        TaskStatus status
) {} 