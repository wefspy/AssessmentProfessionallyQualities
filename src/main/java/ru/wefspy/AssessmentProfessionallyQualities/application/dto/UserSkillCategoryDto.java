package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.Collection;

public record UserSkillCategoryDto(
        SkillCategoryDto category,
        Collection<SkillDto> skills,
        Double rating
) {
}
