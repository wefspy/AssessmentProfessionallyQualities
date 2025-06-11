package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.application.dto.TeamMemberStatsDTO;

import java.util.List;

@Repository
public class JdbcTeamMemberStatsRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TeamMemberStatsDTO> teamMemberStatsRowMapper = (rs, rowNum) -> {
        TeamMemberStatsDTO stats = new TeamMemberStatsDTO();
        stats.setTeamId(rs.getLong("team_id"));
        stats.setUserId(rs.getLong("user_id"));
        stats.setFirstName(rs.getString("first_name"));
        stats.setMiddleName(rs.getString("middle_name"));
        stats.setLastName(rs.getString("last_name"));
        stats.setSkillCategoryId(rs.getLong("skill_category_id"));
        stats.setSkillCategoryName(rs.getString("skill_category_name"));
        stats.setSkillCategoryColor(rs.getString("skill_category_color"));
        stats.setAverageRating(rs.getDouble("average_rating"));
        return stats;
    };

    public JdbcTeamMemberStatsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TeamMemberStatsDTO> findTeamMemberStatsByTeamId(Long teamId) {
        return jdbcTemplate.query(
                """
                SELECT 
                    tm.team_id,
                    tm.user_id,
                    ui.first_name,
                    ui.middle_name,
                    ui.last_name,
                    tm.skill_category_id,
                    sc.name as skill_category_name,
                    sc.color as skill_category_color,
                    COALESCE(AVG(us.rating) / 2, 0) as average_rating
                FROM team_members tm
                JOIN users_info ui ON tm.user_id = ui.id
                JOIN skill_categories sc ON tm.skill_category_id = sc.id
                LEFT JOIN skills s ON s.skill_category_id = tm.skill_category_id
                LEFT JOIN users_skills us ON us.user_id = tm.user_id AND us.skill_id = s.id
                WHERE tm.team_id = ?
                GROUP BY 
                    tm.team_id,
                    tm.user_id,
                    ui.first_name,
                    ui.middle_name,
                    ui.last_name,
                    tm.skill_category_id,
                    sc.name,
                    sc.color
                ORDER BY ui.last_name, ui.first_name
                """,
                teamMemberStatsRowMapper,
                teamId
        );
    }
} 