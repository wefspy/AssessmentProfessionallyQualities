package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;

    public UserRole() {}

    public UserRole(Long userId,
                    Long roleId) {
        setUserId(userId);
        setRoleId(roleId);
    }

    public UserRole(Long id,
                    Long userId,
                    Long roleId) {
        this(userId, roleId);
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRole userRole = (UserRole) o;

        return Objects.equals(getId(), userRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
