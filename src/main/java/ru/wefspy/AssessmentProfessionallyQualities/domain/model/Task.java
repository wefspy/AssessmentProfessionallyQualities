package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import ru.wefspy.AssessmentProfessionallyQualities.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private Long id;
    private Long evaluatorMemberId;
    private Long assigneeMemberId;
    private Long leadMemberId;
    private String title;
    private String description;
    private LocalDate deadlineCompletion;
    private TaskStatus status;

    public Task() {}

    public Task(Long evaluatorMemberId,
                Long assigneeMemberId,
                Long leadMemberId,
                String title,
                String description,
                LocalDate deadlineCompletion,
                TaskStatus status) {
        setEvaluatorMemberId(evaluatorMemberId);
        setAssigneeMemberId(assigneeMemberId);
        setLeadMemberId(leadMemberId);
        setTitle(title);
        setDescription(description);
        setDeadlineCompletion(deadlineCompletion);
        setStatus(status);
    }

    public Task(Long id,
                Long evaluatorMemberId,
                Long assigneeMemberId,
                Long leadMemberId,
                String title,
                String description,
                LocalDate deadlineCompletion,
                TaskStatus status) {
        this(evaluatorMemberId, assigneeMemberId, leadMemberId, title, description, deadlineCompletion, status);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEvaluatorMemberId() {
        return evaluatorMemberId;
    }

    public void setEvaluatorMemberId(Long evaluatorMemberId) {
        this.evaluatorMemberId = evaluatorMemberId;
    }

    public Long getAssigneeMemberId() {
        return assigneeMemberId;
    }

    public void setAssigneeMemberId(Long assigneeMemberId) {
        this.assigneeMemberId = assigneeMemberId;
    }

    public Long getLeadMemberId() {
        return leadMemberId;
    }

    public void setLeadMemberId(Long leadMemberId) {
        this.leadMemberId = leadMemberId;
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
                ", evaluatorMemberId=" + evaluatorMemberId +
                ", assigneeMemberId=" + assigneeMemberId +
                ", leadMemberId=" + leadMemberId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadlineCompletion=" + deadlineCompletion +
                ", status=" + status +
                '}';
    }
}
