package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getLong("evaluator_id"),
                rs.getLong("assignee_id"),
                rs.getLong("lead_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("deadline_completion").toLocalDate(),
                TaskStatus.valueOf(rs.getString("status"))
        );
    }
}
