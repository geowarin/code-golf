package org.jetbrains.codeGolf.plugin.writer;

import com.google.common.base.Preconditions;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;


public final class CreateTransformThisClassTaskAction extends AdminActionBase {

    protected void doAdminAction(Project project, AnActionEvent anActionEvent, String username, String password) {
        Preconditions.checkNotNull(project, "doAdminAction");
        Preconditions.checkNotNull(anActionEvent, "doAdminAction");
        Preconditions.checkNotNull(username, "doAdminAction");
        Preconditions.checkNotNull(password, "doAdminAction");
        Editor editor;
        DataKey tmp28_25 = PlatformDataKeys.EDITOR;
        Preconditions.checkNotNull(tmp28_25, "PlatformDataKeys", "EDITOR");
        DataKey tmp36_28 = tmp28_25;
        if (tmp36_28 != null) {
            DataContext tmp44_41 = anActionEvent.getDataContext();
            Preconditions.checkNotNull(tmp44_41, "AnActionEvent", "getDataContext");
            // TODO ???
//            tmpTernaryOp = ((Editor) tmp36_28.getData(tmp44_41));
        }
    }
}