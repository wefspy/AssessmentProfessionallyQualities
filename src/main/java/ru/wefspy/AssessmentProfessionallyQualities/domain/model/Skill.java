package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class Skill {
    private Long id;
    private Long skillCategoryId;
    private String name;
    private Boolean isNecessary;

    public Skill() {}

    public Skill(Long skillCategoryId,
                 String name) {
        setSkillCategoryId(skillCategoryId);
        setName(name);
        setIsNecessary(false);
    }

    public Skill(Long id,
                 Long skillCategoryId,
                 String name) {
        this(skillCategoryId, name);
        setId(id);
    }

    public Skill(Long id,
                 Long skillCategoryId,
                 String name,
                 Boolean isNecessary) {
        this(id, skillCategoryId, name);
        setIsNecessary(isNecessary);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSkillCategoryId() {
        return skillCategoryId;
    }

    public void setSkillCategoryId(Long skillCategoryId) {
        this.skillCategoryId = skillCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Skill skill = (Skill) o;

        return Objects.equals(getId(), skill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", skillCategoryId=" + skillCategoryId +
                ", name='" + name + '\'' +
                ", isNecessary=" + isNecessary +
                '}';
    }
}
