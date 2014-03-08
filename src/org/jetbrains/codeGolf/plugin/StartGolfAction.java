package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.jgoodies.common.base.Strings;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;

import java.util.List;


public final class StartGolfAction extends AnAction {
    private boolean isRecording = false;


    public final boolean getIsRecording() {
        return this.isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public void update(AnActionEvent e) {
        if (e != null) {
            Presentation presentation = e.getPresentation();
            // TODO ??
        }
    }


    public void actionPerformed(AnActionEvent anActionEvent) {
        if (!this.isRecording) {
            String serverUrl = CodeGolfConfigurableAccessor.getServerUrl();
            List<GolfTask> tasks = RestClientUtil.loadTasks(serverUrl);

            Project project = anActionEvent.getProject();
            String userName = CodeGolfConfigurableAccessor.getUserName();
            String password = null;
            if (Strings.isNotBlank(userName)) {
                password = CodeGolfConfigurableAccessor.getUserPassword(project);
            }
            List<UserScore> userScores = Lists.newArrayList();
            if (Strings.isNotBlank(userName)) {
                userScores = RestClientUtil.loadScores(serverUrl, userName);
            }
            StartGolfDialog startGolfDialog = new StartGolfDialog(project, tasks, userScores);
            startGolfDialog.show();
            GolfTask selectedTask = startGolfDialog.getSelectedTask();
            if (selectedTask != null && startGolfDialog.isOK()) {
                startTask(selectedTask, project, userName, password);
            }
        }
    }

    private void startTask(GolfTask task, Project project, String username, String password) {
        VirtualFile file = createFile(project, task.getTaskName(), task.getInitialCode());
        Preconditions.checkNotNull(file);

        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, task.getInitialOffset());
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        if (fileEditorManager != null) {
//            isRecording = true;
            fileEditorManager.openTextEditor(descriptor, true);
            Document document = FileDocumentManager.getInstance().getDocument(file);

            ActionsRecorder recorder = new ActionsRecorder(task, project, document, username, password, null);
            RecordingControlPanel recordingControlPanel = new RecordingControlPanel(project, document, task.getTargetCode(), recorder);
            recorder.setControlPanel(recordingControlPanel);
            recordingControlPanel.showHint();


            recorder.startRecording();
        }
    }

    private VirtualFile createFile(final Project project, final String taskName, final String text) {
        if (text == null)
            return null;

        if (text.length() > 0) {

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
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);

        String className = fileName + ".java";
        PsiFile tempFile = psiFileFactory.createFileFromText(className, StdFileTypes.JAVA, text);
        VirtualFile file = baseDir.findChild(className);
//        selectedVFile = LocalFileSystem.getInstance().findFileByIoFile(fileChooser.getSelectedFile());
        // Get document file

        if (file != null && file.exists()) {
            Document docFile = FileDocumentManager.getInstance().getDocument(file);
            docFile.setText(text);
            return file;
//            file.delete(this);
        }
        PsiFile added = (PsiFile) root.add(tempFile);
        return added.getVirtualFile();
    }

    public StartGolfAction() {
        super("Start Code Golf...");
    }
}