package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class TeamMember {
    private Long id;
    private Long teamId;
    private Long userId;
    private String role;

    public TeamMember() {}

    public TeamMember(Long teamId,
                      Long userId,
                      String role) {
        setTeamId(teamId);
        setUserId(userId);
        setRole(role);
    }

    public TeamMember(Long id,
                      Long teamId,
                      Long userId,
                      String role) {
        this(teamId, userId, role);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
                ", role='" + role + '\'' +
                '}';
    }
}
