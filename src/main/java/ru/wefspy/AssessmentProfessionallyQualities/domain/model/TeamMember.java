package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class TeamMember {
    private Long id;
    private Long teamId;
    private Long userId;
    private Long skillCategoryId;
    private String skillCategoryName;

    public TeamMember() {}

    public TeamMember(Long teamId,
                      Long userId,
                      Long skillCategoryId) {
        setTeamId(teamId);
        setUserId(userId);
        setSkillCategoryId(skillCategoryId);
    }

    public TeamMember(Long id,
                      Long teamId,
                      Long userId,
                      Long skillCategoryId) {
        this(teamId, userId, skillCategoryId);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkillCategoryId() {
        return skillCategoryId;
    }

    public void setSkillCategoryId(Long skillCategoryId) {
        this.skillCategoryId = skillCategoryId;
    }

    public String getSkillCategoryName() {
        return skillCategoryName;
    }

    public void setSkillCategoryName(String skillCategoryName) {
        this.skillCategoryName = skillCategoryName;
    }

    // This method is used to maintain compatibility with old code that expects getRole()
    public String getRole() {
        return skillCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamMember that = (TeamMember) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "id=" + id +
                ", teamId=" + teamId +
                ", userId=" + userId +
                ", skillCategoryId=" + skillCategoryId +
                ", skillCategoryName='" + skillCategoryName + '\'' +
                '}';
    }
}
