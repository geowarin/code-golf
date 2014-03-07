package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import jet.JetObject;

import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;


public final class LoginWithJBAccountAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        AnActionEvent tmp1_0 = e;
        if (tmp1_0 == null) throw new NullPointerException();
        Project tmp11_8 = tmp1_0.getProject();
        if (tmp11_8 == null) throw new NullPointerException();
        Project project = tmp11_8;
        new StringBuilder().append("User name ");
    }
}