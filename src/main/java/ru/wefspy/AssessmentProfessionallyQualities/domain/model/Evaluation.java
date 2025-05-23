package ru.wefspy.AssessmentProfessionallyQualities.domain.model;

import java.util.Objects;

public class Evaluation {
    private Long id;
    private Long taskId;
    private Long userSkillId;
    private Short evaluation;
    private String feedback;

    public Evaluation() {}

    public Evaluation(Long taskId,
                      Long userSkillId,
                      Short evaluation,
                      String feedback) {
        setTaskId(taskId);
        setUserSkillId(userSkillId);
        setEvaluation(evaluation);
        setFeedback(feedback);
    }

    public Evaluation(Long id,
                      Long taskId,
                      Long userSkillId,
                      Short evaluation,
                      String feedback) {
        this(taskId, userSkillId, evaluation, feedback);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserSkillId() {
        return userSkillId;
    }

    public void setUserSkillId(Long userSkillId) {
        this.userSkillId = userSkillId;
    }

    public Short getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Short evaluation) {
        this.evaluation = evaluation;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Evaluation that = (Evaluation) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", userSkillId=" + userSkillId +
                ", evaluation=" + evaluation +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
