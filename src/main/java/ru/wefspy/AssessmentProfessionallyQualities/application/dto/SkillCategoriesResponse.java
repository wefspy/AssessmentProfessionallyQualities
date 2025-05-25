package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.Collection;

public record SkillCategoriesResponse(
        Collection<SkillCategoryDto> categories
) {
} 