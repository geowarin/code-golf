package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;


public final class LoginWithJBAccountAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        Project eProject = e.getProject();
        if (eProject == null) throw new NullPointerException();
        Project project = eProject;
        new StringBuilder().append("User name ");
    }
}