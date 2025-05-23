package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class User {
    private Long id;
    private String username;
    private String passwordHash;

    public User() {}

    public User(String username,
                String passwordHash) {
        setUsername(username);
        setPasswordHash(passwordHash);
    }

    public User(Long id,
                String username,
                String passwordHash) {
        this(username, passwordHash);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;

        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
