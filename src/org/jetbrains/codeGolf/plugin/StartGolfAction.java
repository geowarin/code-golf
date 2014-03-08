package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
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
//            tmpTernaryOp = presentation;
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
        Editor editor;
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        if (fileEditorManager != null)
            fileEditorManager.openTextEditor(descriptor, true);
    }

    private VirtualFile createFile(Project project, String taskName, String text) {
        String className;
        if (text == null)
            return null;

        if (text.length() > 0) {
            PsiFile tempFile;
            PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
            if (psiFileFactory != null) {
                PsiFile psiFile = psiFileFactory.createFileFromText("A.java", StdFileTypes.JAVA, text);
                Preconditions.checkNotNull(psiFile, "PsiFileFactory", "createFileFromText");
                return psiFile.getVirtualFile();
            }
        }
        return null;
    }

    public StartGolfAction() {
        super("Start Code Golf...");
    }
}