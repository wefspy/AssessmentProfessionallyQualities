package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.Education;

import java.util.Objects;

public class UserInfo {
    private Long id;
    private Long mainSkillCategoryId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private Short courseNumber;
    private Education education;

    public UserInfo() {
    }

    public UserInfo(Long mainSkillCategoryId,
                    String email,
                    String firstName,
                    String middleName,
                    String lastName,
                    Short courseNumber,
                    Education education) {
        setMainSkillCategoryId(mainSkillCategoryId);
        setEmail(email);
        setFirstName(firstName);
        setMiddleName(middleName);
        setLastName(lastName);
        setCourseNumber(courseNumber);
        setEducation(education);
    }

    public UserInfo(Long id,
                    Long mainSkillCategoryId,
                    String email,
                    String firstName,
                    String middleName,
                    String lastName,
                    Short courseNumber,
                    Education education) {
        this(
                mainSkillCategoryId,
                email,
                firstName,
                middleName,
                lastName,
                courseNumber,
                education
        );
        setId(id);
    }

    public UserInfo(Long id,
                    String email,
                    String firstName,
                    String middleName,
                    String lastName) {
        setId(id);
        setEmail(email);
        setFirstName(firstName);
        setMiddleName(middleName);
        setLastName(lastName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMainSkillCategoryId() {
        return mainSkillCategoryId;
    }

    public void setMainSkillCategoryId(Long mainSkillCategoryId) {
        this.mainSkillCategoryId = mainSkillCategoryId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Short getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Short courseNumber) {
        this.courseNumber = courseNumber;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;

        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", mainSkillCategoryId=" + mainSkillCategoryId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", courseNumber=" + courseNumber +
                ", education=" + education +
                '}';
    }
}
