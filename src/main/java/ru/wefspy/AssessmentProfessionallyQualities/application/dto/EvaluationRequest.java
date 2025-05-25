package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

import java.util.Collection;

public record EvaluationRequest(
    Collection<SkillEvaluation> skillEvaluations
) {
    public record SkillEvaluation(
        Long skillId,
        Short rating,
        String feedback
    ) {}
} 