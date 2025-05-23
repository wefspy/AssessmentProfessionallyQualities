package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private Long id;
    private Long evaluatorId;
    private Long assigneeId;
    private Long leadId;
    private String title;
    private String description;
    private LocalDate deadlineCompletion;
    private TaskStatus status;

    public Task() {}

    public Task(Long evaluatorId,
                Long assigneeId,
                Long leadId,
                String title,
                String description,
                LocalDate deadlineCompletion,
                TaskStatus status) {
        setEvaluatorId(evaluatorId);
        setAssigneeId(assigneeId);
        setLeadId(leadId);
        setTitle(title);
        setDescription(description);
        setDeadlineCompletion(deadlineCompletion);
        setStatus(status);
    }

    public Task(Long id,
                Long evaluatorId,
                Long assigneeId,
                Long leadId,
                String title,
                String description,
                LocalDate deadlineCompletion,
                TaskStatus status) {
        this(evaluatorId, assigneeId, leadId, title, description, deadlineCompletion, status);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(Long evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadlineCompletion() {
        return deadlineCompletion;
    }

    public void setDeadlineCompletion(LocalDate deadlineCompletion) {
        this.deadlineCompletion = deadlineCompletion;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;

        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", evaluatorId=" + evaluatorId +
                ", assigneeId=" + assigneeId +
                ", leadId=" + leadId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadlineCompletion=" + deadlineCompletion +
                ", status=" + status +
                '}';
    }
}
