package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.dto.AccessTokenInput;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.enums.JwtClaims;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.enums.JwtTokenType;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.property.JwtProperties;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenFactory {
    private final JwtProperties jwtProperties;
    private final JwtKeyProvider jwtKeyProvider;

    public JwtTokenFactory(JwtProperties jwtProperties,
                           JwtKeyProvider jwtKeyProvider) {
        this.jwtProperties = jwtProperties;
        this.jwtKeyProvider = jwtKeyProvider;
    }

    public String generateAccessToken(AccessTokenInput payload) {
        return getBaseBuilder(payload.userId(), JwtTokenType.ACCESS)
                .claim(JwtClaims.USERNAME.getKey(), payload.username())
                .claim(JwtClaims.ROLES.getKey(), payload.roles())
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return getBaseBuilder(userId, JwtTokenType.REFRESH).compact();
    }

    private JwtBuilder getBaseBuilder(Long userId, JwtTokenType type) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .id(UUID.randomUUID().toString())
                .claim(JwtClaims.TOKEN_TYPE.getKey(), type.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(jwtKeyProvider.getSignKey());
    }
}
