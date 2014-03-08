package org.jetbrains.codeGolf.plugin;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;


public final class NavigateToEditorAction extends AnAction {
    private final Document document;

    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = editor.getProject();
//        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        VirtualFile file = fileDocumentManager.getFile(document);
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, 0);

        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        fileEditorManager.openTextEditor(descriptor, true);
    }


    public final Document getDocument() {
        return this.document;
    }

    public NavigateToEditorAction(Document document) {
        super("Navigate to editor", "Navigate to editor", AllIcons.General.AutoscrollToSource);
        this.document = document;
    }
}