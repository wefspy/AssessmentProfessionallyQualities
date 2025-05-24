package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Team;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.TeamRowMapper;

import java.util.Collection;
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
        return jdbcTemplate.queryForObject(
                "INSERT INTO teams (name) " +
                        "VALUES (?) " +
                        "RETURNING * ",
                teamRowMapper,
                team.getName()
        );
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
