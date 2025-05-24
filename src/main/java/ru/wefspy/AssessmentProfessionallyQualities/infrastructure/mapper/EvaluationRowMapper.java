package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Evaluation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EvaluationRowMapper implements RowMapper<Evaluation> {
    @Override
    public Evaluation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Evaluation(
                rs.getLong("id"),
                rs.getLong("task_id"),
                rs.getLong("user_skill_id"),
                rs.getShort("evaluation"),
                rs.getString("feedback")
        );
    }
}
