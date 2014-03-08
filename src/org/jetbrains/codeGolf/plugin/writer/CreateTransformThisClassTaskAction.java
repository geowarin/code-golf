package org.jetbrains.codeGolf.plugin.writer;

import com.google.common.base.Preconditions;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;


public final class CreateTransformThisClassTaskAction extends AdminActionBase {

    protected void doAdminAction(Project project, AnActionEvent anActionEvent, String username, String password) {
        Preconditions.checkNotNull(project, "doAdminAction");
        Preconditions.checkNotNull(anActionEvent, "doAdminAction");
        Preconditions.checkNotNull(username, "doAdminAction");
        Preconditions.checkNotNull(password, "doAdminAction");

        Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());
        // TODO ??
    }
}