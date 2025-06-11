package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Collection;

public record UpdateTaskRequest(
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
        TaskStatus status,

        @NotNull(message = "User skill IDs to evaluate are required")
        @Size(min = 1, message = "At least one user skill ID must be specified")
        Collection<Long> userSkillIds
) {} 