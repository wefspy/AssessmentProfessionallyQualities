package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.RoleRowMapper;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcRoleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RoleRowMapper roleRowMapper;

    public JdbcRoleRepository(JdbcTemplate jdbcTemplate,
                              RoleRowMapper roleRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.roleRowMapper = roleRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM roles ", Long.class);
    }

    public Role save(Role role) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO roles (name) " +
                        "VALUES (?)",
                new String[]{"id"}
            );
            ps.setString(1, role.getName());
            return ps;
        }, keyHolder);

        role.setId(keyHolder.getKey().longValue());
        return role;
    }

    public void saveAll(Collection<Role> roles) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO roles (name) " +
                        "VALUES (?) ",
                roles,
                roles.size(),
                (ps, role) -> {
                    ps.setString(1, role.getName());
                }
        );
    }

    public Optional<Role> findById(Long id) {
        List<Role> roles = jdbcTemplate.query(
                "SELECT * " +
                        "FROM roles " +
                        "WHERE id = ? ",
                roleRowMapper,
                id
        );

        return roles.stream().findFirst();
    }

    public List<Role> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT r.id, r.name " +
                        "FROM roles r " +
                        "JOIN users_roles ur ON r.id = ur.role_id " +
                        "WHERE ur.user_id = ? ",
                roleRowMapper,
                userId
        );
    }

    public List<Role> findAllByName(Collection<String> names) {
        if (names == null || names.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(names.size(), "?"));

        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM roles " +
                        "WHERE name IN (%s)".formatted(placeholders),
                roleRowMapper,
                names.toArray()
        );
    }

    public Role update(Role role) {
        jdbcTemplate.update(
                "UPDATE roles " +
                        "SET " +
                        "name = ? " +
                        "WHERE id = ? ",
                role.getName(),
                role.getId()
        );

        return role;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM roles WHERE id = ? ", id);
    }
}
