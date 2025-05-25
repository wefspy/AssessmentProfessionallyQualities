package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
        Long evaluatorMemberId,
        Long assigneeMemberId,
        Long leadMemberId,
        String title,
        String description,
        LocalDate deadlineCompletion,
        TaskStatus status
) {} 