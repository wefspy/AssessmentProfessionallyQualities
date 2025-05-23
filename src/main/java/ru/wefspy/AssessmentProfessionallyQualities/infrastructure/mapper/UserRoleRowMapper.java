package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRoleRowMapper implements RowMapper<UserRole> {
    @Override
    public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserRole(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("role_id")
        );
    }
}
