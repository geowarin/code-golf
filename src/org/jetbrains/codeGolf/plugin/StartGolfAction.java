package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import kotlin.KotlinPackage;


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
            Preconditions.checkNotNull(presentation, "AnActionEvent", "getPresentation");
            tmpTernaryOp = presentation;
        }
    }


    public void actionPerformed(AnActionEvent anActionEvent) {
        if (!this.isRecording) {
            Project project;
            if (anActionEvent != null) tmpTernaryOp = anActionEvent.getProject();
        }
    }


    private final void startTask(GolfTask task, Project project, String username, String password) {
        VirtualFile file = createFile(project, task.getTaskName(), task.getInitialCode());
        Preconditions.checkNotNull(file);

        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, task.getInitialOffset());
        Editor editor;
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        if (fileEditorManager != null)
            fileEditorManager.openTextEditor(descriptor, true);
    }


    private final VirtualFile createFile(Project project, String taskName, String text) {
        String className;
        if (text == null)
            return null;

        if (text.length() > 0) {
            PsiFile tempFile;
            PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
            if (psiFileFactory != null) {
                Preconditions.checkNotNull(StdFileTypes.JAVA, "StdFileTypes", "JAVA");
                if (text == null) throw new NullPointerException();
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