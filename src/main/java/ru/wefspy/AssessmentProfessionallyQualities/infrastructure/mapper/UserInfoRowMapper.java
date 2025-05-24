package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.Education;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserInfoRowMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        String educationStr = rs.getString("education");
        Education education = educationStr != null ? Education.valueOf(educationStr) : null;
        
        return new UserInfo(
                rs.getLong("id"),
                rs.getLong("main_skill_category_id"),
                rs.getString("email"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getShort("course_number"),
                education
        );
    }
}
