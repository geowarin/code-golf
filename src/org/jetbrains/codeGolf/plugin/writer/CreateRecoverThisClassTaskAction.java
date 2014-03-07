package org.jetbrains.codeGolf.plugin.writer;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import org.jetbrains.codeGolf.plugin.GolfTask;


public final class CreateRecoverThisClassTaskAction extends AdminActionBase
  implements JetObject
{

  protected void doAdminAction( Project project,  AnActionEvent anActionEvent,  String username,  String password)
  {
    Intrinsics.checkParameterIsNotNull(project, "doAdminAction"); Intrinsics.checkParameterIsNotNull(anActionEvent, "doAdminAction"); Intrinsics.checkParameterIsNotNull(username, "doAdminAction"); Intrinsics.checkParameterIsNotNull(password, "doAdminAction");
    Editor editor;
    DataKey tmp28_25 = PlatformDataKeys.EDITOR; Intrinsics.checkFieldIsNotNull(tmp28_25, "PlatformDataKeys", "EDITOR");
    DataKey tmp36_28 = tmp28_25; if (tmp36_28 != null)
    {
      DataContext tmp44_41 = anActionEvent.getDataContext(); Intrinsics.checkReturnedValueIsNotNull(tmp44_41, "AnActionEvent", "getDataContext"); tmpTernaryOp = ((Editor)tmp36_28.getData(tmp44_41));
    }
  }
}