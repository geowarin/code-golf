package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.login.Credentials;

/**
* Date: 10/03/2014
* Time: 23:42
*
* @author Geoffroy Warin (http://geowarin.github.io)
*/
class GolfGame {
    private final Project project;
    private Credentials credentials;
    private final GolfTask task;

    public GolfGame(Project project, Credentials credentials, GolfTask task) {
        this.project = project;
        this.credentials = credentials;
        this.task = task;
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

    public void start() {
        Document document = createFile(task, project);
        ActionsRecorder recorder = new ActionsRecorder(task, project, document, credentials.getUserName(), credentials.getToken());

        recorder.setRestarter(new ActionsRecorder.Restarter() {
            @Override
            public void restart() {
                start();
            }
        });

        RecordingControlPanel recordingControlPanel = new RecordingControlPanel(project, document, task.getTargetCode(), recorder);
        recorder.setControlPanel(recordingControlPanel);

        recorder.startRecording();
    }

    public boolean isRecording() {
        // FIXME
        return false;
    }
}
