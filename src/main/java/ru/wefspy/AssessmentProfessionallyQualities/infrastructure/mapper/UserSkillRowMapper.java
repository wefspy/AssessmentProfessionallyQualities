package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserSkill;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserSkillRowMapper implements RowMapper<UserSkill> {
    @Override
    public UserSkill mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserSkill(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("skill_id"),
                rs.getShort("rating")
        );
    }
}
