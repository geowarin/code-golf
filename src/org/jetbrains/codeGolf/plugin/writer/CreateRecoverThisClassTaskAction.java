package org.jetbrains.codeGolf.plugin.writer;

import com.google.common.base.Preconditions;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import jet.JetObject;

import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import org.jetbrains.codeGolf.plugin.GolfTask;


public final class CreateRecoverThisClassTaskAction extends AdminActionBase {

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
            DataContext dataContext = anActionEvent.getDataContext();
            Preconditions.checkNotNull(dataContext, "AnActionEvent", "getDataContext");
            tmpTernaryOp = ((Editor) tmp36_28.getData(dataContext));
        }
    }
}