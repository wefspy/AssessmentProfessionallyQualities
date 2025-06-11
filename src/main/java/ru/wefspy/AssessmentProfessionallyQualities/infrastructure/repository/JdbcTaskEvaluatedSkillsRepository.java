package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class JdbcTaskEvaluatedSkillsRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTaskEvaluatedSkillsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(Long taskId, Collection<Long> userSkillIds) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO task_evaluated_skills (task_id, user_skill_id) VALUES (?, ?)",
                userSkillIds,
                userSkillIds.size(),
                (ps, userSkillId) -> {
                    ps.setLong(1, taskId);
                    ps.setLong(2, userSkillId);
                }
        );
    }

    public List<Long> findAllByTaskId(Long taskId) {
        return jdbcTemplate.queryForList(
                "SELECT user_skill_id FROM task_evaluated_skills WHERE task_id = ?",
                Long.class,
                taskId
        );
    }

    public void deleteByTaskId(Long taskId) {
        jdbcTemplate.update("DELETE FROM task_evaluated_skills WHERE task_id = ?", taskId);
    }
} 