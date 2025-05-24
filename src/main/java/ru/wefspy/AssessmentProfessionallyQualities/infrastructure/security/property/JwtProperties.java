package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Objects;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;
    private Duration accessExpiration;
    private Duration refreshExpiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getAccessExpiration() {
        return accessExpiration;
    }

    public void setAccessExpiration(Duration accessExpiration) {
        this.accessExpiration = accessExpiration;
    }

    public Duration getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(Duration refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JwtProperties that = (JwtProperties) o;

        return Objects.equals(getSecret(), that.getSecret())
                && Objects.equals(getAccessExpiration(), that.getAccessExpiration())
                && Objects.equals(getRefreshExpiration(), that.getRefreshExpiration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSecret(),
                getAccessExpiration(),
                getRefreshExpiration());
    }

    @Override
    public String toString() {
        return "JwtProperties{" +
                "secret='" + secret + '\'' +
                ", accessExpiration=" + accessExpiration +
                ", refreshExpiration=" + refreshExpiration +
                '}';
    }
}
