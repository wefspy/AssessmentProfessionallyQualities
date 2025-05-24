package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserRole;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.UserRoleRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRoleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRoleRowMapper userRoleRowMapper;

    public JdbcUserRoleRepository(JdbcTemplate jdbcTemplate,
                                  UserRoleRowMapper userRoleRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRoleRowMapper = userRoleRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users_roles ", Long.class);
    }

    public UserRole save(UserRole userRole) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users_roles (user_id, role_id) " +
                        "VALUES (?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, userRole.getUserId());
            ps.setLong(2, userRole.getRoleId());
            return ps;
        }, keyHolder);

        userRole.setId(keyHolder.getKey().longValue());
        return userRole;
    }

    public void saveAll(Collection<UserRole> usersRoles) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO users_roles (user_id, role_id) " +
                        "VALUES (?, ?) ",
                usersRoles,
                usersRoles.size(),
                (ps, userRole) -> {
                    ps.setLong(1, userRole.getUserId());
                    ps.setLong(2, userRole.getRoleId());
                }
        );
    }

    public Optional<UserRole> findById(Long id) {
        List<UserRole> usersRoles = jdbcTemplate.query(
                "SELECT * " +
                        "FROM users_roles " +
                        "WHERE id = ? ",
                userRoleRowMapper,
                id
        );

        return usersRoles.stream().findFirst();
    }

    public UserRole update(UserRole userRole) {
        jdbcTemplate.update(
                "UPDATE users_roles " +
                        "SET " +
                        "user_id = ?, " +
                        "role_id = ? " +
                        "WHERE id = ?",
                userRole.getUserId(),
                userRole.getRoleId(),
                userRole.getId()
        );

        return userRole;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users_roles WHERE id = ?", id);
    }
}
