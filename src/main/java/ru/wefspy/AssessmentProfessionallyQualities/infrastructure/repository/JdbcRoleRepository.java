package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Role;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.RoleRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        return jdbcTemplate.queryForObject(
                "INSERT INTO roles (name) " +
                        "VALUES (?) " +
                        "RETURNING * ",
                roleRowMapper,
                role.getName()
        );
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
