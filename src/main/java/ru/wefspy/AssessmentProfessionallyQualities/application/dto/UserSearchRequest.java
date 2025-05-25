package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.List;

public record UserSearchRequest(
        String query,
        List<SkillSearchCriteria> skillCriteria
) {
} 