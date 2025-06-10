package ru.wefspy.AssessmentProfessionallyQualities.application.dto;

public class TeamMemberStatsDTO {
    private Long teamId;
    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Long skillCategoryId;
    private String skillCategoryName;
    private Double averageRating;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}