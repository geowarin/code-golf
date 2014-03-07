package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

import java.io.File;
import java.util.List;

import jet.FunctionImpl0;
import jet.JetObject;
import jet.Unit;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;
import kotlin.Pair;


public final class StartGolfAction extends AnAction
        implements JetObject {
    private boolean isRecording = false;


    public final boolean getIsRecording() {
        return this.isRecording;
    }


    public final void setIsRecording(boolean<set-?>) {
        this.isRecording =<set - ?>;
    }


    public void update(AnActionEvent e) {
        AnActionEvent tmp1_0 = e;
        if (tmp1_0 != null) {
            Presentation tmp8_5 = tmp1_0.getPresentation();
            Preconditions.checkNotNull(tmp8_5, "AnActionEvent", "getPresentation");
            tmpTernaryOp = tmp8_5;
        }
    }


    public void actionPerformed(AnActionEvent anActionEvent) {
        if (!this.isRecording) {
            Project project;
            AnActionEvent tmp8_7 = anActionEvent;
            if (tmp8_7 != null) tmpTernaryOp = tmp8_7.getProject();
        }
    }


    private final void startTask(GolfTask task, Project project, String username, String password) {
        VirtualFile file2 = createFile(project, task.getTaskName(), task.getInitialCode());
        if (file2 != null) 1;
        if (0 != 0) return;
        VirtualFile file = file2;

        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, task.getInitialOffset());
        Editor editor;
        FileEditorManager tmp51_48 = FileEditorManager.getInstance(project);
        if (tmp51_48 != null) tmpTernaryOp = tmp51_48.openTextEditor(descriptor, 1);
    }


    private final VirtualFile createFile(Project project, String taskName, String text) {
        String className;
        if (text == null) 1;
        if ((0 != 0 ? KotlinPackage.length(text) <= 0 ? 0 : 1 : 0) != 0) {
            PsiFile tempFile;
            PsiFileFactory tmp34_31 = PsiFileFactory.getInstance(project);
            if (tmp34_31 != null) {
                LanguageFileType tmp44_41 = StdFileTypes.JAVA;
                Preconditions.checkNotNull(tmp44_41, "StdFileTypes", "JAVA");
                String tmp58_57 = text;
                if (tmp58_57 == null) throw new NullPointerException();
                PsiFile tmp71_68 = tmp34_31.createFileFromText("A.java", (FileType) tmp44_41, (CharSequence) tmp58_57);
                Preconditions.checkNotNull(tmp71_68, "PsiFileFactory", "createFileFromText");
                tmpTernaryOp = tmp71_68;
            }
        }
    }

    @JetConstructor
    public StartGolfAction() {
        super("Start Code Golf...");
    }
}