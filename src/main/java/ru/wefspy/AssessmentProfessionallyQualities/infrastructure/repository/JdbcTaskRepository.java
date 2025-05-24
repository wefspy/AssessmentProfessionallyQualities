package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Task;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.TaskRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTaskRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper taskRowMapper;

    public JdbcTaskRepository(JdbcTemplate jdbcTemplate,
                              TaskRowMapper taskRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskRowMapper = taskRowMapper;
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM tasks", Long.class);
    }

    public Task save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tasks (evaluator_id, assignee_id, lead_id, title, description, deadline_completion, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, task.getEvaluatorId());
            ps.setLong(2, task.getAssigneeId());
            ps.setLong(3, task.getLeadId());
            ps.setString(4, task.getTitle());
            ps.setString(5, task.getDescription());
            ps.setDate(6, Date.valueOf(task.getDeadlineCompletion()));
            ps.setString(7, task.getStatus().name());
            return ps;
        }, keyHolder);

        task.setId(keyHolder.getKey().longValue());
        return task;
    }

    public void saveAll(Collection<Task> tasks) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO tasks (evaluator_id, assignee_id, lead_id, title, description, deadline_completion, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ",
                tasks,
                tasks.size(),
                (ps, task) -> {
                    ps.setLong(1, task.getEvaluatorId());
                    ps.setLong(2, task.getAssigneeId());
                    ps.setLong(3, task.getLeadId());
                    ps.setString(4, task.getTitle());
                    ps.setString(5, task.getDescription());
                    ps.setDate(6, Date.valueOf(task.getDeadlineCompletion()));
                    ps.setString(7, task.getStatus().name());
                }
        );
    }

    public Optional<Task> findById(Long id) {
        List<Task> tasks = jdbcTemplate.query(
                "SELECT * " +
                        "FROM tasks " +
                        "WHERE id = ?",
                taskRowMapper,
                id
        );

        return tasks.stream().findFirst();
    }

    public Task update(Task task) {
        jdbcTemplate.update(
                "UPDATE tasks " +
                        "SET " +
                        "evaluator_id = ?," +
                        "assignee_id = ?, " +
                        "lead_id = ?, " +
                        "title = ?, " +
                        "description = ?, " +
                        "deadline_completion = ?, " +
                        "status = ? " +
                        "WHERE id = ?",
                task.getEvaluatorId(),
                task.getAssigneeId(),
                task.getLeadId(),
                task.getTitle(),
                task.getDescription(),
                Date.valueOf(task.getDeadlineCompletion()),
                task.getStatus().name(),
                task.getId()
        );

        return task;
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }
}
