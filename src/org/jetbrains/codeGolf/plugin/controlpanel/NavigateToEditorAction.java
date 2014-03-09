package org.jetbrains.codeGolf.plugin.controlpanel;

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
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;


@SuppressWarnings("ComponentNotRegistered")
public final class NavigateToEditorAction extends AnAction {
    private final Document document;

    public NavigateToEditorAction(Document document) {
        super("Navigate to editor", "Navigate to editor", AllIcons.General.AutoscrollToSource);
        this.document = document;
    }

    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        FileEditorManager.getInstance(e.getProject()).openFile(file, true);
    }
}