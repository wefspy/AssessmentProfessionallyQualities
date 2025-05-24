package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.parser;

import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.JwtKeyProvider;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.enums.JwtTokenType;

@Component
public class RefreshTokenParser extends AbstractJwtTokenParser {
    private final JwtTokenType jwtTokenType = JwtTokenType.REFRESH;

    public RefreshTokenParser(JwtKeyProvider jwtKeyProvider) {
        super(jwtKeyProvider);
    }

    @Override
    public Boolean ofType(String token) {
        return ofType(token, jwtTokenType);
    }
}
