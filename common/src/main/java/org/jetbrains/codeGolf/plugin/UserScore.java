package org.jetbrains.codeGolf.plugin;

public final class UserScore {
    private String taskId;
    private int userSolution;
    private int bestSolution;
    private double score;

    public UserScore(String taskId, int userSolution, int bestSolution, double score) {
        this.taskId = taskId;
        this.userSolution = userSolution;
        this.bestSolution = bestSolution;
        this.score = score;
    }

    public UserScore() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getUserSolution() {
        return userSolution;
    }

    public void setUserSolution(int userSolution) {
        this.userSolution = userSolution;
    }

    public int getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(int bestSolution) {
        this.bestSolution = bestSolution;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}