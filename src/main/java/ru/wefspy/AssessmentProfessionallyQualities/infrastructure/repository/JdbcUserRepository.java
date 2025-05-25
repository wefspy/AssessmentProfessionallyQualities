package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.User;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.UserRowMapper;

import java.sql.PreparedStatement;
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
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users (username, password_hash) " +
                        "VALUES (?, ?)",
                new String[]{"id"}
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
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

    public Optional<User> findByUsername(String username) {
        List<User> users = jdbcTemplate.query(
                "SELECT * " +
                        "FROM users " +
                        "WHERE username = ?",
                userRowMapper,
                username
        );

        return users.stream().findFirst();
    }

    public Boolean existsByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT exists (SELECT 1 FROM users WHERE username = ?)",
                Boolean.class,
                username
        );
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

    public List<User> findAll(Pageable pageable) {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?",
                userRowMapper,
                pageable.getPageSize(),
                pageable.getOffset()
        );
    }
}
