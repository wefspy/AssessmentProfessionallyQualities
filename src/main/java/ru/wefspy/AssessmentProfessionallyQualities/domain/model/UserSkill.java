package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class UserSkill {
    private Long id;
    private Long userId;
    private Long skillId;
    private Short rating;

    public UserSkill() {}

    public UserSkill(Long userId,
                     Long skillId,
                     Short rating) {
        setUserId(userId);
        setSkillId(skillId);
        setRating(rating);
    }

    public UserSkill(Long id,
                     Long userId,
                     Long skillId,
                     Short rating) {
        this(userId, skillId, rating);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSkill userSkill = (UserSkill) o;

        return Objects.equals(getId(), userSkill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
