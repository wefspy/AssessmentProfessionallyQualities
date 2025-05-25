package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;

public record TaskWithMembersDto(
        Long id,
        TeamMemberInfoDto evaluator,
        TeamMemberInfoDto assignee,
        TeamMemberInfoDto lead,
        String title,
        String description,
        LocalDate deadlineCompletion,
        TaskStatus status
) {} 