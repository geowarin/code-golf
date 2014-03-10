package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;

import javax.swing.*;

public class IdeaUtils {
    public static Editor getEditor(Project project, Document document) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        TextEditor selectedEditor = (TextEditor) FileEditorManager.getInstance(project).getSelectedEditor(file);
        return selectedEditor.getEditor();
    }

    public static void showBalloon(String htmlText) {
        JComponent statusBar = WindowManager.getInstance().getIdeFrame(null).getStatusBar().getComponent();
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(htmlText, MessageType.WARNING, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .showInCenterOf(statusBar);
    }

    public static void setFocusOnFile(Project project, VirtualFile file, int initialOffset) {
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, initialOffset);
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        fileEditorManager.openTextEditor(descriptor, true);
    }

    public static VirtualFile createOrReplaceFile(Project project, String text) {
        String className = getJavaClassRealName(project, text);
        VirtualFile baseDir = getSourceRoot(project);

        VirtualFile file = baseDir.findChild(className);
        if (file != null && file.exists()) {
            replaceContent(file, text);
            return file;
        }

        return createFileInDirectory(project, baseDir, className, text);
    }

    private static void replaceContent(VirtualFile file, String text) {
        Document docFile = FileDocumentManager.getInstance().getDocument(file);
        assert docFile != null;
        docFile.setText(text);
    }

    private static VirtualFile createFileInDirectory(Project project, VirtualFile baseDir, String className, String text) {
        PsiFile javaFile = PsiFileFactory.getInstance(project).createFileFromText(className, StdFileTypes.JAVA, text);
        PsiDirectory root = PsiManager.getInstance(project).findDirectory(baseDir);
        PsiFile added = (PsiFile) root.add(javaFile);
        return added.getVirtualFile();
    }

    private static VirtualFile getSourceRoot(Project project) {
        VirtualFile[] sourceRoots = ProjectRootManager.getInstance(project).getContentSourceRoots();
        return sourceRoots.length > 0 ? sourceRoots[0] : project.getBaseDir();
    }

    private static String getJavaClassRealName(Project project, String text) {
        PsiJavaFile tempFile = (PsiJavaFile) PsiFileFactory.getInstance(project).createFileFromText("temp.java", StdFileTypes.JAVA, text);
        return tempFile.getClasses()[0].getName() + ".java";
    }

    //        KeyEvent keyEvent = IdeEventQueue.getInstance().getKeyEventDispatcher().getContext().getInputEvent();
    //        AWTEvent currentEvent = IdeEventQueue.getCurrentEvent();
    //        selectedVFile = LocalFileSystem.getInstance().findFileByIoFile(fileChooser.getSelectedFile());

}
