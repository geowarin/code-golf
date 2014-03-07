package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Lcom/intellij/openapi/actionSystem/AnAction;", abiVersion=6)
public final class LoginWithJBAccountAction extends AnAction
  implements JetObject
{
  @JetMethod(returnType="V")
  public void actionPerformed(@JetValueParameter(name="e", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent e)
  {
    AnActionEvent tmp1_0 = e; if (tmp1_0 == null) Intrinsics.throwNpe();
    Project tmp11_8 = tmp1_0.getProject(); if (tmp11_8 == null) Intrinsics.throwNpe(); Project project = tmp11_8;
    new StringBuilder().append("User name ");
  }
}