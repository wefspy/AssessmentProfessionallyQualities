package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.SkillCategoryRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSkillCategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SkillCategoryRowMapper skillCategoryRowMapper;

    public JdbcSkillCategoryRepository(JdbcTemplate jdbcTemplate,
                                       SkillCategoryRowMapper skillCategoryRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.skillCategoryRowMapper = skillCategoryRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM skill_categories", Long.class);
    }

    public SkillCategory save(SkillCategory skillCategory) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO skill_categories (name) " +
                        "VALUES (?) " +
                        "RETURNING * ",
                skillCategoryRowMapper,
                skillCategory.getName()
        );
    }

    public void saveAll(Collection<SkillCategory> skillCategories) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO skill_categories (name) " +
                        "VALUES (?)",
                skillCategories,
                skillCategories.size(),
                (ps, skillCategory) -> {
                    ps.setString(1, skillCategory.getName());
                }
        );
    }

    public Optional<SkillCategory> findById(Long id) {
        List<SkillCategory> skillCategories = jdbcTemplate.query(
                "SELECT * " +
                        "FROM skill_categories " +
                        "WHERE id = ? ",
                skillCategoryRowMapper,
                id
        );

        return skillCategories.stream().findFirst();
    }

    public SkillCategory update(SkillCategory skillCategory) {
        jdbcTemplate.update(
                "UPDATE skill_categories " +
                        "SET " +
                        "name = ? " +
                        "WHERE id = ? ",
                skillCategory.getName(),
                skillCategory.getId()
        );

        return skillCategory;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM skill_categories WHERE id = ? ", id);
    }
}
