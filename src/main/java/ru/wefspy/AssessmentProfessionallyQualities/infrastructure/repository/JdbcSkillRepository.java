package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Skill;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.SkillRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSkillRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SkillRowMapper skillRowMapper;

    public JdbcSkillRepository(JdbcTemplate jdbcTemplate,
                               SkillRowMapper skillRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.skillRowMapper = skillRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM skills ", Long.class);
    }

    public Skill save(Skill skill) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO skills (skill_category_id, name) " +
                        "VALUES (?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, skill.getSkillCategoryId());
            ps.setString(2, skill.getName());
            return ps;
        }, keyHolder);

        skill.setId(keyHolder.getKey().longValue());
        return skill;
    }

    public void saveAll(Collection<Skill> skills) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO skills (skill_category_id, name) " +
                        "VALUES (?, ?) ",
                skills,
                skills.size(),
                (ps, skill) -> {
                    ps.setLong(1, skill.getSkillCategoryId());
                    ps.setString(2, skill.getName());
                }
        );
    }

    public Optional<Skill> findById(Long id) {
        List<Skill> skills = jdbcTemplate.query(
                "SELECT * " +
                        "FROM skills " +
                        "WHERE id = ? ",
                skillRowMapper,
                id
        );

        return skills.stream().findFirst();
    }

    public Skill update(Skill skill) {
        jdbcTemplate.update(
                "UPDATE skills " +
                        "SET " +
                        "skill_category_id = ?, " +
                        "name = ? " +
                        "WHERE id = ? ",
                skill.getSkillCategoryId(),
                skill.getName(),
                skill.getId()
        );

        return skill;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM skills WHERE id = ? ", id);
    }
}
