package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Team;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.TeamMember;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.TeamRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTeamRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TeamRowMapper teamRowMapper;

    public JdbcTeamRepository(JdbcTemplate jdbcTemplate,
                              TeamRowMapper teamRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamRowMapper = teamRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM teams", Long.class);
    }

    public Team save(Team team) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO teams (name) " +
                            "VALUES (?) ",
                    new String[]{"id"}
            );
            ps.setString(1, team.getName());
            return ps;
        }, keyHolder);

        team.setId(keyHolder.getKey().longValue());
        return team;
    }

    public void saveAll(Collection<Team> teams) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO teams (name) " +
                        "VALUES (?) ",
                teams,
                teams.size(),
                (ps, team) -> {
                    ps.setString(1, team.getName());
                }
        );
    }

    public Optional<Team> findById(Long id) {
        List<Team> teams = jdbcTemplate.query(
                "SELECT * " +
                        "FROM teams " +
                        "WHERE id = ? ",
                teamRowMapper,
                id
        );

        return teams.stream().findFirst();
    }

    public List<Team> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM teams WHERE id IN (%s)", placeholders),
                teamRowMapper,
                ids.toArray()
        );
    }

    public Team update(Team team) {
        jdbcTemplate.update(
                "UPDATE teams " +
                        "SET " +
                        "name = ? " +
                        "WHERE id = ? ",
                team.getName(),
                team.getId()
        );

        return team;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM teams WHERE id = ?", id);
    }
}
