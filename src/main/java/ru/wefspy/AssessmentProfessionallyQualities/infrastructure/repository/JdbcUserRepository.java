package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.UserRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate,
                              UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users", Long.class);
    }

    public User save(User user) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO users (username, password_hash) " +
                        "VALUES (?, ?) " +
                        "RETURNING * ",
                userRowMapper,
                user.getUsername(),
                user.getPasswordHash()
        );
    }

    public void saveAll(Collection<User> users) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO users (username, password_hash) " +
                        "VALUES (?, ?) ",
                users,
                users.size(),
                (ps, user) -> {
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPasswordHash());
                }
        );
    }

    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * " +
                        "FROM users " +
                        "WHERE id = ?",
                userRowMapper,
                id
        );

        return users.stream().findFirst();
    }

    public User update(User user) {
        jdbcTemplate.update(
                "UPDATE users " +
                        "SET " +
                        "username = ?, " +
                        "password_hash = ? " +
                        "WHERE id = ? ",
                user.getUsername(),
                user.getPasswordHash(),
                user.getId()
        );

        return user;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ? ", id);
    }
}
