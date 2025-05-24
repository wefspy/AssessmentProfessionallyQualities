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
        return new TeamMember(
                rs.getLong("id"),
                rs.getLong("team_id"),
                rs.getLong("user_id"),
                rs.getString("role")
        );
    }
}
