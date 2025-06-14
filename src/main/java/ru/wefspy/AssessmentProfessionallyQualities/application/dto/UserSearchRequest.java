package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.List;

public record UserSearchRequest(
        String query,
        List<SkillSearchCriteria> skillCriteria,
        Short courseNumber,
        Long mainSkillCategoryId,
        Double minAverageRating
) {
    public Double minAverageRating() {
        return minAverageRating != null ? minAverageRating : 0.0;
    }
} 