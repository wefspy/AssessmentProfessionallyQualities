package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.TeamMember;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.TeamMemberRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTeamMemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TeamMemberRowMapper teamMemberRowMapper;

    public JdbcTeamMemberRepository(JdbcTemplate jdbcTemplate,
                                    TeamMemberRowMapper teamMemberRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamMemberRowMapper = teamMemberRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM team_members", Long.class);
    }

    public List<TeamMember> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT tm.*, sc.name as skill_category_name " +
                        "FROM team_members tm " +
                        "LEFT JOIN skill_categories sc ON tm.skill_category_id = sc.id " +
                        "WHERE tm.id IN (%s)", placeholders),
                teamMemberRowMapper,
                ids.toArray()
        );
    }

    public TeamMember save(TeamMember teamMember) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO team_members (team_id, user_id, skill_category_id) " +
                        "VALUES (?, ?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, teamMember.getTeamId());
            ps.setLong(2, teamMember.getUserId());
            ps.setLong(3, teamMember.getSkillCategoryId());
            return ps;
        }, keyHolder);

        teamMember.setId(keyHolder.getKey().longValue());
        return teamMember;
    }

    public void saveAll(Collection<TeamMember> teamMembers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO team_members (team_id, user_id, skill_category_id) " +
                        "VALUES (?, ?, ?) ",
                teamMembers,
                teamMembers.size(),
                (ps, teamMember) -> {
                    ps.setLong(1, teamMember.getTeamId());
                    ps.setLong(2, teamMember.getUserId());
                    ps.setLong(3, teamMember.getSkillCategoryId());
                }
        );
    }

    public Optional<TeamMember> findById(Long id) {
        List<TeamMember> teamMembers = jdbcTemplate.query(
                "SELECT tm.*, sc.name as skill_category_name " +
                        "FROM team_members tm " +
                        "LEFT JOIN skill_categories sc ON tm.skill_category_id = sc.id " +
                        "WHERE tm.id = ?",
                teamMemberRowMapper,
                id
        );

        return teamMembers.stream().findFirst();
    }

    public List<TeamMember> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT tm.*, sc.name as skill_category_name " +
                        "FROM team_members tm " +
                        "LEFT JOIN skill_categories sc ON tm.skill_category_id = sc.id " +
                        "WHERE tm.user_id = ?",
                teamMemberRowMapper,
                userId
        );
    }

    public TeamMember update(TeamMember teamMember) {
        jdbcTemplate.update(
                "UPDATE team_members " +
                        "SET " +
                        "team_id = ?, " +
                        "user_id = ?, " +
                        "skill_category_id = ? " +
                        "WHERE id = ?",
                teamMember.getTeamId(),
                teamMember.getUserId(),
                teamMember.getSkillCategoryId(),
                teamMember.getId()
        );

        return teamMember;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM team_members WHERE id = ?", id);
    }
}
