package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.Education;

import java.util.Collection;

public record PageableUserDto(
        Long userId,
        String firstName,
        String middleName,
        String lastName,
        Collection<TeamMemberDto> teams,
        Short numberCourse,
        Education education,
        SkillCategoryDto mainSkillCategory,
        Collection<SkillDto> topSkills,
        Double averageSkillRating
) {
} 