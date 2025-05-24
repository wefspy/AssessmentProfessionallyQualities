package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.property.JwtProperties;

import javax.crypto.SecretKey;

@Component
public class JwtKeyProvider {
    private final JwtProperties jwtProperties;

    public JwtKeyProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
