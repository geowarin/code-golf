package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.login.Credentials;
import org.jetbrains.codeGolf.plugin.login.LoginService;
import org.jetbrains.codeGolf.plugin.task.GolfTaskManager;
import org.jetbrains.codeGolf.plugin.task.TaskManager;

import java.util.List;


public final class StartGolfAction extends AnAction {

    private ActionsRecorder recorder;

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
            if (selectedTask != null && startGolfDialog.isOK())
                startTask(selectedTask, project, credentials.getUserName(), credentials.getToken());
        }
    }

    private void startTask(final GolfTask task, final Project project, final String username, final String password) {
        VirtualFile file = createFile(project, task.getInitialCode());
        IdeaUtils.setFocusOnFile(project, file, task.getInitialOffset());

        Document document = FileDocumentManager.getInstance().getDocument(file);
        recorder = new ActionsRecorder(task, project, document, username, password);
        recorder.setRestarter(new ActionsRecorder.Restarter() {
            @Override
            public void restart() {
                startTask(task, project, username, password);
            }
        });

        RecordingControlPanel recordingControlPanel = new RecordingControlPanel(project, document, task.getTargetCode(), recorder);
        recorder.setControlPanel(recordingControlPanel);

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

    public boolean isRecording() {
        return recorder != null && recorder.isRecording();
    }
}