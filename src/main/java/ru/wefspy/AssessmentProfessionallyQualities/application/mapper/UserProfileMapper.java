package ru.wefspy.AssessmentProfessionallyQualities.application.mapper;

import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.RoleDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.RegisterResponseDto;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserInfo;

import java.util.Collection;

@Component
public class UserProfileMapper {
    private final RoleMapper roleMapper;

    public UserProfileMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public RegisterResponseDto toDto(User user, Collection<Role> roles) {
        Collection<RoleDto> rolesDto = roles.stream()
                .map(roleMapper::toDto)
                .toList();

        return new RegisterResponseDto(
                user.getId(),
                user.getUsername(),
                rolesDto
        );
    }
}
