package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security;

public enum RoleEnum {
    USER,
    ADMIN;

    public String getAuthority() {
        return String.format("ROLE_%s", name());
    }
}
