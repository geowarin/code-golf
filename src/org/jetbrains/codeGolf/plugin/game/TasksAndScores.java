package org.jetbrains.codeGolf.plugin.game;

import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.UserScore;

import java.util.List;

/**
 * Date: 16/03/2014
 * Time: 21:13
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public class TasksAndScores {
    private final List<GolfTask> tasks;
    private final List<UserScore> scores;

    public TasksAndScores(List<GolfTask> tasks, List<UserScore> scores) {
        this.tasks = tasks;
        this.scores = scores;
    }

    public List<UserScore> getScores() {
        return scores;
    }

    public List<GolfTask> getTasks() {
        return tasks;
    }
}
