package ru.wefspy.AssessmentProfessionallyQualities.application.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.UserDetailsImpl;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.security.jwt.dto.AccessTokenInput;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AccessTokenInputMapper {
    public AccessTokenInput from(UserDetailsImpl user) {
        return new AccessTokenInput(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }

    public AccessTokenInput from(User user, Collection<Role> roles) {
        return new AccessTokenInput(
                user.getId(),
                user.getUsername(),
                roles.stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}
