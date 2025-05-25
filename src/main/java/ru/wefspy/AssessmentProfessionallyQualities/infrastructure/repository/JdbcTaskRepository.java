package ru.wefspy.AssessmentProfessionallyQualities.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;
import ru.wefspy.AssessmentProfessionallyQualities.domain.model.Task;
import ru.wefspy.AssessmentProfessionallyQualities.infrastructure.mapper.TaskRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Long countByEvaluatorMemberIds(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return 0L;
        }

        String placeholders = String.join(",", Collections.nCopies(memberIds.size(), "?"));
        String sql = String.format(
                "SELECT COUNT(*) FROM tasks WHERE evaluator_member_id IN (%s)",
                placeholders
        );

        return jdbcTemplate.queryForObject(sql, Long.class, memberIds.toArray());
    }

    public List<Task> findByEvaluatorMemberIds(List<Long> memberIds, Pageable pageable) {
        if (memberIds.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(memberIds.size(), "?"));
        
        // Build the ORDER BY clause based on the sort properties
        StringBuilder orderBy = new StringBuilder();
        if (pageable.getSort().isSorted()) {
            orderBy.append("ORDER BY ");
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "status" -> orderBy.append("status").append(order.isAscending() ? " ASC" : " DESC");
                    case "deadlineCompletion" -> orderBy.append("deadline_completion").append(order.isAscending() ? " ASC" : " DESC");
                    default -> orderBy.append("deadline_completion ASC"); // default sort
                }
                orderBy.append(", ");
            });
            // Remove trailing comma and space
            if (orderBy.length() > 2) {
                orderBy.setLength(orderBy.length() - 2);
            }
        } else {
            // Default sorting if none specified
            orderBy.append("ORDER BY deadline_completion ASC");
        }

        String sql = String.format(
                "SELECT * FROM tasks WHERE evaluator_member_id IN (%s) %s LIMIT ? OFFSET ?",
                placeholders,
                orderBy
        );

        Object[] params = new Object[memberIds.size() + 2];
        System.arraycopy(memberIds.toArray(), 0, params, 0, memberIds.size());
        params[memberIds.size()] = pageable.getPageSize();
        params[memberIds.size() + 1] = pageable.getOffset();

        return jdbcTemplate.query(sql, taskRowMapper, params);
    }

    public Task save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tasks (evaluator_member_id, assignee_member_id, lead_member_id, title, description, deadline_completion, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new String[]{"id"}
            );
            ps.setLong(1, task.getEvaluatorMemberId());
            ps.setLong(2, task.getAssigneeMemberId());
            ps.setLong(3, task.getLeadMemberId());
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
                "INSERT INTO tasks (evaluator_member_id, assignee_member_id, lead_member_id, title, description, deadline_completion, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ",
                tasks,
                tasks.size(),
                (ps, task) -> {
                    ps.setLong(1, task.getEvaluatorMemberId());
                    ps.setLong(2, task.getAssigneeMemberId());
                    ps.setLong(3, task.getLeadMemberId());
                    ps.setString(4, task.getTitle());
                    ps.setString(5, task.getDescription());
                    ps.setDate(6, Date.valueOf(task.getDeadlineCompletion()));
                    ps.setString(7, task.getStatus().name());
                }
        );
    }

    public Optional<Task> findById(Long id) {
        List<Task> tasks = jdbcTemplate.query(
                "SELECT * FROM tasks WHERE id = ?",
                taskRowMapper,
                id
        );

        return tasks.stream().findFirst();
    }

    public Task update(Task task) {
        jdbcTemplate.update(
                "UPDATE tasks " +
                        "SET " +
                        "evaluator_member_id = ?, " +
                        "assignee_member_id = ?, " +
                        "lead_member_id = ?, " +
                        "title = ?, " +
                        "description = ?, " +
                        "deadline_completion = ?, " +
                        "status = ? " +
                        "WHERE id = ?",
                task.getEvaluatorMemberId(),
                task.getAssigneeMemberId(),
                task.getLeadMemberId(),
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

    public boolean existsById(Long id) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE id = ?",
                Long.class,
                id
        );
        return count != null && count > 0;
    }

    public void updateStatus(Long taskId, TaskStatus status) {
        jdbcTemplate.update(
                "UPDATE tasks SET status = ?::task_status WHERE id = ?",
                status.name(),
                taskId
        );
    }
}

