package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Evaluation;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.EvaluationRowMapper;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcEvaluationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EvaluationRowMapper evaluationRowMapper;

    public JdbcEvaluationRepository(JdbcTemplate jdbcTemplate,
                                    EvaluationRowMapper evaluationRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.evaluationRowMapper = evaluationRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM evaluations", Long.class);
    }

    public boolean existsByUserSkillId(Long userSkillId) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM evaluations WHERE user_skill_id = ?",
                Long.class,
                userSkillId
        );
        return count != null && count > 0;
    }

    public Evaluation save(Evaluation evaluation) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO evaluations (task_id, user_skill_id, evaluation, feedback) " +
                        "VALUES (?, ?, ?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, evaluation.getTaskId());
            ps.setLong(2, evaluation.getUserSkillId());
            ps.setShort(3, evaluation.getEvaluation());
            if (evaluation.getFeedback() != null) {
                ps.setString(4, evaluation.getFeedback());
            } else {
                ps.setNull(4, Types.NULL);
            }
            return ps;
        }, keyHolder);

        evaluation.setId(keyHolder.getKey().longValue());
        return evaluation;
    }

    public void saveAll(Collection<Evaluation> evaluations) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO evaluations (task_id, user_skill_id, evaluation, feedback) " +
                        "VALUES (?, ?, ?, ?) ",
                evaluations,
                evaluations.size(),
                (ps, evaluation) -> {
                    ps.setLong(1, evaluation.getTaskId());
                    ps.setLong(2, evaluation.getUserSkillId());
                    ps.setShort(3, evaluation.getEvaluation());
                    ps.setString(4, evaluation.getFeedback());
                }
        );
    }

    public Optional<Evaluation> findById(Long id) {
        List<Evaluation> evaluations = jdbcTemplate.query(
                "SELECT * " +
                        "FROM evaluations " +
                        "WHERE id = ? ",
                evaluationRowMapper,
                id
        );

        return evaluations.stream().findFirst();
    }

    public List<Evaluation> findAllByUserSkillId(Long userSkillId) {
        return jdbcTemplate.query(
                "SELECT * FROM evaluations WHERE user_skill_id = ?",
                evaluationRowMapper,
                userSkillId
        );
    }

    public Evaluation update(Evaluation evaluation) {
        jdbcTemplate.update(
                "UPDATE evaluations " +
                        "SET " +
                        "task_id = ?, " +
                        "user_skill_id = ?, " +
                        "evaluation = ?, " +
                        "feedback = ? " +
                        "WHERE id = ? ",
                evaluation.getTaskId(),
                evaluation.getUserSkillId(),
                evaluation.getEvaluation(),
                evaluation.getFeedback()
        );

        return evaluation;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM evaluations WHERE id = ? ", id);
    }
} 