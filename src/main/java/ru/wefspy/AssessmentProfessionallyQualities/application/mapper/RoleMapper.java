package ru.wefspy.AssessmentProfessionallyQualities.application.mapper;

import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.RoleDto;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;

@Component
public class RoleMapper {
    public RoleDto toDto(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}
