package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.TeamMember;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeamMemberRowMapper implements RowMapper<TeamMember> {
    @Override
    public TeamMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeamMember member = new TeamMember(
                rs.getLong("id"),
                rs.getLong("team_id"),
                rs.getLong("user_id"),
                rs.getLong("skill_category_id")
        );
        try {
            member.setSkillCategoryName(rs.getString("skill_category_name"));
        } catch (SQLException e) {
            // Ignore if skill_category_name is not in the result set
        }
        return member;
    }
}
