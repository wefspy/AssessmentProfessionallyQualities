package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Collection;

public record TaskWithMembersDto(
        Long id,
        TeamMemberInfoDto evaluator,
        TeamMemberInfoDto assignee,
        TeamMemberInfoDto lead,
        String title,
        String description,
        LocalDate deadlineCompletion,
        TaskStatus status,
        Collection<Long> userSkillIds
) {
    public TaskWithMembersDto(
            Long id,
            TeamMemberInfoDto evaluator,
            TeamMemberInfoDto assignee,
            TeamMemberInfoDto lead,
            String title,
            String description,
            LocalDate deadlineCompletion,
            TaskStatus status) {
        this(id, evaluator, assignee, lead, title, description, deadlineCompletion, status, null);
    }
} 