package org.jetbrains.codeGolf.plugin;

public final class ExportableGolfSolution extends GolfSolution {
    private String creationDate;
    private String taskName;

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ExportableGolfSolution(String creationDate, String taskName, String taskId, String userLogin, int movingCount, int typingCount, int actionsCount, String usedActions) {
        super(taskId, userLogin, movingCount, typingCount, actionsCount, usedActions);
        this.creationDate = creationDate;
        this.taskName = taskName;
    }

    public ExportableGolfSolution() {
    }
}