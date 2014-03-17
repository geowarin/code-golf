package org.jetbrains.codeGolf.plugin.game;

import com.google.common.eventbus.Subscribe;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.IdeaUtils;
import org.jetbrains.codeGolf.plugin.StartGolfDialog;
import org.jetbrains.codeGolf.plugin.event.Events;
import org.jetbrains.codeGolf.plugin.event.StartGameEvent;
import org.jetbrains.codeGolf.plugin.event.StopGameEvent;
import org.jetbrains.codeGolf.plugin.login.Credentials;
import org.jetbrains.codeGolf.plugin.login.LoginService;
import org.jetbrains.codeGolf.plugin.recording.ActionsRecorder;

/**
 * Date: 15/03/2014
 * Time: 11:17
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public class Game {
    private boolean isStarted;
    private TasksAndScores tasksAndScores;

    public Game() {
        Events.register(this);
    }

    public static Game get() {
        return ServiceManager.getService(Game.class);
    }

    public boolean isStarted() {
        return isStarted;
    }

    @Subscribe
    public void onGameStart(StartGameEvent event) {
        Project project = event.getProject();
        Credentials credentials = LoginService.getInstance().showDialogAndLogin(project);
        // User cancels
        if (credentials == null)
            return;

        loadTasksAndScores(project, credentials);
        GolfTask selectedTask = showSelectTaskDialog(project);
        if (selectedTask != null) {
            isStarted = true;
            start(selectedTask, project, credentials);
        }
    }

    @Subscribe
    public void onGameStop(StopGameEvent event) {
        isStarted = false;
    }

    public void loadTasksAndScores(Project project, Credentials credentials) {
        tasksAndScores = ProgressManager.getInstance()
                .runProcessWithProgressSynchronously(new TaskAndScoreLoadTask(credentials.getUserName()), "Loading tasks...", true, project);
    }

    public GolfTask showSelectTaskDialog(Project project) {
        StartGolfDialog startGolfDialog = new StartGolfDialog(project, tasksAndScores.getTasks(), tasksAndScores.getScores());
        startGolfDialog.show();
        GolfTask selectedTask = startGolfDialog.getSelectedTask();
        if (selectedTask != null && startGolfDialog.isOK())
            return selectedTask;
        return null;
    }

    public void start(final GolfTask task, final Project project, final Credentials credentials) {
        Document document = createFile(task, project);
        ActionsRecorder.Restarter restarter = new ActionsRecorder.Restarter() {
            @Override
            public void restart() {
                start(task, project, credentials);
            }
        };
        ActionsRecorder recorder = new ActionsRecorder(task, project, document, credentials, restarter);
        recorder.startRecording();
    }

    private VirtualFile createFile(final Project project, final String text) {
        return ApplicationManager.getApplication().runWriteAction(new Computable<VirtualFile>() {
            @Override
            public VirtualFile compute() {
                return IdeaUtils.createOrReplaceFile(project, text);
            }
        });
    }

    private Document createFile(GolfTask task, Project project) {
        VirtualFile file = createFile(project, task.getInitialCode());
        IdeaUtils.setFocusOnFile(project, file, task.getInitialOffset());
        return FileDocumentManager.getInstance().getDocument(file);
    }
}
