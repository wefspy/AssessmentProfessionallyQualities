package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SkillCategoryRowMapper implements RowMapper<SkillCategory> {
    @Override
    public SkillCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SkillCategory(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
