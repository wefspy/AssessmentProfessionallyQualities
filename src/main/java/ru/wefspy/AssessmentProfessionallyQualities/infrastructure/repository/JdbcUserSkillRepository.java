package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.UserSkill;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.UserSkillRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcUserSkillRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserSkillRowMapper userSkillRowMapper;

    public JdbcUserSkillRepository(JdbcTemplate jdbcTemplate,
                                   UserSkillRowMapper userSkillRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userSkillRowMapper = userSkillRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users_skills", Long.class);
    }

    public Optional<UserSkill> findByUserIdAndSkillId(Long userId, Long skillId) {
        List<UserSkill> userSkills = jdbcTemplate.query(
                "SELECT * FROM users_skills WHERE user_id = ? AND skill_id = ?",
                userSkillRowMapper,
                userId,
                skillId
        );
        return userSkills.stream().findFirst();
    }

    public UserSkill save(UserSkill userSkill) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users_skills (user_id, skill_id, rating) " +
                        "VALUES (?, ?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, userSkill.getUserId());
            ps.setLong(2, userSkill.getSkillId());
            ps.setDouble(3, userSkill.getRating());
            return ps;
        }, keyHolder);

        userSkill.setId(keyHolder.getKey().longValue());
        return userSkill;
    }

    public void saveAll(Collection<UserSkill> usersSkills) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO users_skills (user_id, skill_id, rating)  " +
                        "VALUES (?, ?, ?) ",
                usersSkills,
                usersSkills.size(),
                (ps, userSkill) -> {
                    ps.setLong(1, userSkill.getUserId());
                    ps.setLong(2, userSkill.getSkillId());
                    ps.setDouble(3, userSkill.getRating());
                }
        );
    }

    public Optional<UserSkill> findById(Long id) {
        List<UserSkill> usersSkills = jdbcTemplate.query(
                "SELECT * " +
                        "FROM users_skills " +
                        "WHERE id = ?",
                userSkillRowMapper,
                id
        );

        return usersSkills.stream().findFirst();
    }

    public List<UserSkill> findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM users_skills " +
                        "WHERE user_id = ?",
                userSkillRowMapper,
                userId
        );
    }

    public List<UserSkill> findAllByIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("SELECT * FROM users_skills WHERE id IN (%s)", placeholders);

        return jdbcTemplate.query(
                sql,
                userSkillRowMapper,
                ids.toArray()
        );
    }

    public UserSkill update(UserSkill userSkill) {
        jdbcTemplate.update(
                "UPDATE users_skills " +
                        "SET " +
                        "user_id = ?, " +
                        "skill_id = ?, " +
                        "rating = ? " +
                        "WHERE id = ?",
                userSkill.getUserId(),
                userSkill.getSkillId(),
                userSkill.getRating(),
                userSkill.getId()
        );

        return userSkill;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users_skills WHERE id = ?", id);
    }

    public boolean existsById(Long id) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users_skills WHERE id = ?",
                Long.class,
                id
        );
        return count != null && count > 0;
    }

    public List<UserSkill> findAllByUserIdAndSkillCategoryId(Long userId, Long skillCategoryId) {
        return jdbcTemplate.query(
                "SELECT us.* FROM users_skills us " +
                "JOIN skills s ON us.skill_id = s.id " +
                "WHERE us.user_id = ? AND s.skill_category_id = ?",
                userSkillRowMapper,
                userId,
                skillCategoryId
        );
    }
}
