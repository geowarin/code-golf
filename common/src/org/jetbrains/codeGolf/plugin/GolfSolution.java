package org.jetbrains.codeGolf.plugin;

import jet.JetObject;


public class GolfSolution implements JetObject {
    private String taskId;
    private String userLogin;
    private int movingCount;
    private int typingCount;
    private int actionsCount;
    private String usedActions;

    public int getActionsCount() {
        return actionsCount;
    }

    public void setActionsCount(int actionsCount) {
        this.actionsCount = actionsCount;
    }

    public int getMovingCount() {
        return movingCount;
    }

    public void setMovingCount(int movingCount) {
        this.movingCount = movingCount;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getTypingCount() {
        return typingCount;
    }

    public void setTypingCount(int typingCount) {
        this.typingCount = typingCount;
    }

    public String getUsedActions() {
        return usedActions;
    }

    public void setUsedActions(String usedActions) {
        this.usedActions = usedActions;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public GolfSolution(String taskId, String userLogin, int movingCount, int typingCount, int actionsCount, String usedActions) {
        this.taskId = taskId;
        this.userLogin = userLogin;
        this.movingCount = movingCount;
        this.typingCount = typingCount;
        this.actionsCount = actionsCount;
        this.usedActions = usedActions;
    }

    public GolfSolution() {
    }
}