package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.SkillCategory;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.SkillCategoryRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSkillCategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SkillCategoryRowMapper skillCategoryRowMapper;

    private final RowMapper<SkillCategory> rowMapper = (ResultSet rs, int rowNum) -> {
        SkillCategory category = new SkillCategory();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setColor(rs.getString("color"));
        return category;
    };

    public JdbcSkillCategoryRepository(JdbcTemplate jdbcTemplate,
                                      SkillCategoryRowMapper skillCategoryRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.skillCategoryRowMapper = skillCategoryRowMapper;
    }

    public List<SkillCategory> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM skill_categories WHERE name != 'Soft Skills'",
                rowMapper
        );
    }

    public List<SkillCategory> findAll(Pageable pageable) {
        return jdbcTemplate.query(
                "SELECT * FROM skill_categories WHERE name != 'Soft Skills' ORDER BY id LIMIT ? OFFSET ?",
                rowMapper,
                pageable.getPageSize(),
                pageable.getOffset()
        );
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM skill_categories", Long.class);
    }

    public SkillCategory save(SkillCategory skillCategory) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO skill_categories (name, color) " +
                            "VALUES (?, ?) ",
                    new String[]{"id"}
            );
            ps.setString(1, skillCategory.getName());
            ps.setString(2, skillCategory.getColor());
            return ps;
        }, keyHolder);

        skillCategory.setId(keyHolder.getKey().longValue());
        return skillCategory;
    }

    public void saveAll(Collection<SkillCategory> skillCategories) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO skill_categories (name, color) " +
                        "VALUES (?, ?) ",
                skillCategories,
                skillCategories.size(),
                (ps, skillCategory) -> {
                    ps.setString(1, skillCategory.getName());
                    ps.setString(2, skillCategory.getColor());
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

    public List<SkillCategory> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(
                String.format("SELECT * FROM skill_categories WHERE id IN (%s)", placeholders),
                skillCategoryRowMapper,
                ids.toArray()
        );
    }

    public SkillCategory update(SkillCategory skillCategory) {
        jdbcTemplate.update(
                "UPDATE skill_categories " +
                        "SET " +
                        "name = ?, " +
                        "color = ? " +
                        "WHERE id = ? ",
                skillCategory.getName(),
                skillCategory.getColor(),
                skillCategory.getId()
        );

        return skillCategory;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM skill_categories WHERE id = ?", id);
    }

    public Optional<SkillCategory> findByName(String name) {
        List<SkillCategory> result = jdbcTemplate.query(
                "SELECT * FROM skill_categories WHERE name = ?",
                rowMapper,
                name
        );
        return result.stream().findFirst();
    }
}
