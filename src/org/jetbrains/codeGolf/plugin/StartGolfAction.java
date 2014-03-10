package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.codeGolf.plugin.login.Credentials;
import org.jetbrains.codeGolf.plugin.login.LoginService;
import org.jetbrains.codeGolf.plugin.task.GolfTaskManager;
import org.jetbrains.codeGolf.plugin.task.TaskManager;

import java.util.List;


public final class StartGolfAction extends AnAction {

    private GolfGame game;

    public StartGolfAction() {
        super("Start Code Golf...");
    }

    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(!isRecording());
    }

    public void actionPerformed(AnActionEvent anActionEvent) {
        if (!this.isRecording()) {
            Project project = anActionEvent.getProject();
            Credentials credentials = LoginService.getInstance().showDialogAndLogin(project);
            // User cancels
            if (credentials == null)
                return;

            TaskManager taskManager = new GolfTaskManager();
            List<GolfTask> tasks = taskManager.loadTasks();
            List<UserScore> userScores = taskManager.loadScores(credentials.getUserName());

            StartGolfDialog startGolfDialog = new StartGolfDialog(project, tasks, userScores);
            startGolfDialog.show();
            GolfTask selectedTask = startGolfDialog.getSelectedTask();
            if (selectedTask != null && startGolfDialog.isOK()) {
                game = new GolfGame(project, credentials, selectedTask);
                game.start();
            }
        }
    }

    private boolean isRecording() {
        return game != null && game.isRecording();
    }

}