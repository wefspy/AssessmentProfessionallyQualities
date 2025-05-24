package ru.wefspy.AssessmentProfessionallyQualities.application.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.LoginRequestDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.LoginResponseDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.exception.UserNotFoundException;
import ru.wefspy.AssessmentProfessionallyQualities.application.mapper.AccessTokenInputMapper;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcRoleRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository.JdbcUserRepository;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.JwtTokenFactory;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.dto.AccessTokenInput;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.parser.RefreshTokenParser;

import java.util.Collection;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenFactory jwtTokenFactory;
    private final RefreshTokenParser refreshTokenParser;
    private final JdbcUserRepository userRepository;
    private final JdbcRoleRepository roleRepository;
    private final AccessTokenInputMapper accessTokenInputMapper;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenFactory jwtTokenFactory,
                       RefreshTokenParser refreshTokenParser,
                       JdbcUserRepository userRepository,
                       JdbcRoleRepository roleRepository,
                       AccessTokenInputMapper accessTokenInputMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenFactory = jwtTokenFactory;
        this.refreshTokenParser = refreshTokenParser;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accessTokenInputMapper = accessTokenInputMapper;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AccessTokenInput accessTokenInput = accessTokenInputMapper.from(userDetails);

        return new LoginResponseDto(
                jwtTokenFactory.generateAccessToken(accessTokenInput),
                jwtTokenFactory.generateRefreshToken(accessTokenInput.userId())
        );
    }

    public LoginResponseDto refresh(String refreshToken) {
        Long userId = refreshTokenParser.getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("В токен зашит id %s пользователя, которого не существует", userId))
                );
        Collection<Role> roles = roleRepository.findAllByUserId(userId);

        AccessTokenInput accessTokenInput = accessTokenInputMapper.from(user, roles);

        return new LoginResponseDto(
                jwtTokenFactory.generateAccessToken(accessTokenInput),
                jwtTokenFactory.generateRefreshToken(accessTokenInput.userId())
        );
    }
}
