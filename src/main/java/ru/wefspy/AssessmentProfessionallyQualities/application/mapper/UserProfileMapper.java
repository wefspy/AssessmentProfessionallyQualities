package ru.wefspy.AssessmentProfessionallyQualities.application.mapper;

import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.RoleDto;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.UserProfileDto;
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

    public UserProfileDto toDto(User user, UserInfo userInfo, Collection<Role> roles) {
        Collection<RoleDto> rolesDto = roles.stream()
                .map(roleMapper::toDto)
                .toList();

        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                userInfo.getEmail(),
                userInfo.getFirstName(),
                userInfo.getMiddleName(),
                userInfo.getLastName(),
                userInfo.getCourseNumber(),
                userInfo.getEducation(),
                rolesDto
        );
    }
}
