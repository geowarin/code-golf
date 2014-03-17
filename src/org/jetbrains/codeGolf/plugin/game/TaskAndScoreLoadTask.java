package org.jetbrains.codeGolf.plugin.game;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.ThrowableComputable;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.UserScore;
import org.jetbrains.codeGolf.plugin.task.GolfTaskManager;
import org.jetbrains.codeGolf.plugin.task.TaskManager;

import java.util.List;

/**
 * Date: 16/03/2014
 * Time: 21:12
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public class TaskAndScoreLoadTask implements ThrowableComputable<TasksAndScores, RuntimeException> {
    private final String userName;

    public TaskAndScoreLoadTask(String userName) {
        this.userName = userName;
    }

    @Override
    public TasksAndScores compute() {
        TaskManager taskManager = new GolfTaskManager();
        ProgressManager.getInstance().getProgressIndicator().setFraction(0.1);
        List<GolfTask> tasks = taskManager.loadTasks();
        ProgressManager.getInstance().getProgressIndicator().setFraction(0.5);
        List<UserScore> scores = taskManager.loadScores(userName);
        ProgressManager.getInstance().getProgressIndicator().setFraction(1.0);
        return new TasksAndScores(tasks, scores);
    }
}
