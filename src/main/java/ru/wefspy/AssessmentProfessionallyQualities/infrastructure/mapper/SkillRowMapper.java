package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Skill;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SkillRowMapper implements RowMapper<Skill> {
    @Override
    public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Skill(
                rs.getLong("id"),
                rs.getLong("skill_category_id"),
                rs.getString("name")
        );
    }
}
