package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.login.Credentials;
import org.jetbrains.codeGolf.plugin.recording.ActionsRecorder;
import org.jetbrains.codeGolf.plugin.task.GolfTaskManager;
import org.jetbrains.codeGolf.plugin.task.TaskManager;

import java.util.List;

/**
 * Date: 10/03/2014
 * Time: 23:42
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
class GolfGame {
    private final Project project;
    private final Credentials credentials;
    private List<GolfTask> taskList;
    private List<UserScore> userScores;

    public GolfGame(Project project, Credentials credentials) {
        this.project = project;
        this.credentials = credentials;
    }

    public void loadTasksAndScores() {
        TaskManager taskManager = new GolfTaskManager();
        taskList = taskManager.loadTasks();
        userScores = taskManager.loadScores(credentials.getUserName());
    }

    public GolfTask showSelectTaskDialog() {
        StartGolfDialog startGolfDialog = new StartGolfDialog(project, taskList, userScores);
        startGolfDialog.show();
        GolfTask selectedTask = startGolfDialog.getSelectedTask();
        if (selectedTask != null && startGolfDialog.isOK())
            return selectedTask;
        return null;
    }

    public void start(final GolfTask task) {
        Document document = createFile(task, project);
        ActionsRecorder.Restarter restarter = new ActionsRecorder.Restarter() {
            @Override
            public void restart() {
                start(task);
            }
        };
        ActionsRecorder recorder = new ActionsRecorder(task, project, document, credentials.getUserName(), credentials.getToken(), restarter);

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


    public boolean isRecording() {
        // FIXME
        return false;
    }
}
