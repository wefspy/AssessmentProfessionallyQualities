package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.Collection;

public record UserProfileDto(
        String firstName,
        String middleName,
        String lastName,
        SkillCategoryDto mainSkillCategory,
        SkillDto bestSkill,
        Integer totalNumberSkills,
        Collection<TeamMemberDto> teams,
        Collection<SkillCategoryDto> skillCategories
) {
}
