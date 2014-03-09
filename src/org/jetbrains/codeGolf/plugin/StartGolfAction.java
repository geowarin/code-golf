package org.jetbrains.codeGolf.plugin;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.jgoodies.common.base.Strings;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.login.LoginWithJBAccount;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;
import org.jetbrains.codeGolf.plugin.settings.CodeGolfConfigurableAccessor;

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
            List<GolfTask> tasks = RestClientUtil.loadTasks(CodeGolfConfigurableAccessor.getServerUrl());
            Pair<String,String> credentials = LoginWithJBAccount.showDialogAndLogin(project);

            if (credentials == null)
                return;

            String userName = credentials.getFirst();
            String password = credentials.getSecond();
            List<UserScore> userScores = getUserScores(CodeGolfConfigurableAccessor.getServerUrl(), userName);

            StartGolfDialog startGolfDialog = new StartGolfDialog(project, tasks, userScores);
            startGolfDialog.show();
            GolfTask selectedTask = startGolfDialog.getSelectedTask();
            if (selectedTask != null && startGolfDialog.isOK())
                startTask(selectedTask, project, userName, password);
        }
    }

    private List<UserScore> getUserScores(String serverUrl, String userName) {
        List<UserScore> userScores = Lists.newArrayList();
        if (Strings.isNotBlank(userName)) {
            userScores = RestClientUtil.loadScores(serverUrl, userName);
        }
        return userScores;
    }

    private void startTask(final GolfTask task, final Project project, final String username, final String password) {

        VirtualFile file = createFile(project, task.getTaskName(), task.getInitialCode());
        setFocusOnFile(project, file, task.getInitialOffset());

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

    private void setFocusOnFile(Project project, VirtualFile file, int initialOffset) {
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, initialOffset);
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        fileEditorManager.openTextEditor(descriptor, true);
    }

    private VirtualFile createFile(final Project project, final String taskName, final String text) {
        if (Strings.isNotBlank(text)) {
            return ApplicationManager.getApplication().runWriteAction(new Computable<VirtualFile>() {
                @Override
                public VirtualFile compute() {
                    return createOrReplaceFile(project, taskName, text);
                }
            });
        }
        return null;
    }

    private VirtualFile createOrReplaceFile(Project project, String fileName, String text) {
        VirtualFile baseDir = project.getBaseDir();
        PsiDirectory root = PsiManager.getInstance(project).findDirectory(baseDir);

        String className = fileName + ".java";
        PsiFile tempFile = PsiFileFactory.getInstance(project).createFileFromText(className, StdFileTypes.JAVA, text);
        VirtualFile file = baseDir.findChild(className);
//        selectedVFile = LocalFileSystem.getInstance().findFileByIoFile(fileChooser.getSelectedFile());
        // Get document file

        if (file != null && file.exists()) {
            Document docFile = FileDocumentManager.getInstance().getDocument(file);
            assert docFile != null;
            docFile.setText(text);
            return file;
        }
        PsiFile added = (PsiFile) root.add(tempFile);
        return added.getVirtualFile();
    }

    public boolean isRecording() {
        return recorder != null && recorder.isRecording();
    }
}