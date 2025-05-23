package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class SkillCategory {
    private Long id;
    private String name;

    public SkillCategory() {}

    public SkillCategory(String name) {
        setName(name);
    }

    public SkillCategory(Long id,
                         String name) {
        this(name);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SkillCategory that = (SkillCategory) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SkillCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
