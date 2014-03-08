package org.jetbrains.codeGolf.plugin;

public final class GolfTask {
    private String authorLogin;
    private String taskId;
    private String taskName;
    private String initialCode;
    private int initialOffset;
    private String targetCode;
    private String hint;

    public GolfTask() {
    }

    public GolfTask(String authorLogin, String taskId, String taskName, String initialCode, int initialOffset, String targetCode, String hint) {
        this.authorLogin = authorLogin;
        this.taskId = taskId;
        this.taskName = taskName;
        this.initialCode = initialCode;
        this.initialOffset = initialOffset;
        this.targetCode = targetCode;
        this.hint = hint;
    }

    public final String toHtml() {
        return getTargetCode().replaceAll("\n", "<BR/>").replaceAll(" ", "&nbsp;");
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(String initialCode) {
        this.initialCode = initialCode;
    }

    public int getInitialOffset() {
        return initialOffset;
    }

    public void setInitialOffset(int initialOffset) {
        this.initialOffset = initialOffset;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}