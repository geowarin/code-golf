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

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(!isRecording());
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        if (!isRecording()) {
            Project project = anActionEvent.getProject();
            Credentials credentials = LoginService.getInstance().showDialogAndLogin(project);
            // User cancels
            if (credentials == null)
                return;

            game = new GolfGame(project, credentials);
            game.loadTasksAndScores();
            GolfTask selectedTask = game.showSelectTaskDialog();
            if (selectedTask != null)
                game.start(selectedTask);
        }
    }

    private boolean isRecording() {
        return game != null && game.isRecording();
    }

}