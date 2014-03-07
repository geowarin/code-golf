package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;

import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.Pair;
import org.jetbrains.codeGolf.auth.JBAccountAuthHelper;

public final class LoginWithJBAccount {

    public static final Pair<String, String> showDialogAndLogin(Project project) {
        Preconditions.checkNotNull(project, "showDialogAndLogin");
        String username = CodeGolfConfigurableAccessor. getUserName();
        String password = CodeGolfConfigurableAccessor. getUserPassword(project);

        boolean userNameNotEmpty = !StringUtil.isEmpty(username);
        if (userNameNotEmpty ? StringUtil.isEmpty(password) : true) {
            JBAccountDialog dlg = new JBAccountDialog(project);
            dlg.show();
            if (!dlg.isOK()) {
                return null;
            }
            username = dlg.getUsername();
            password = dlg.getPassword();
        }
        String encodedPassword = JBAccountAuthHelper.encodePassword(password);
        if (encodedPassword == null) throw new NullPointerException();
        return new Pair(username, encodedPassword);
    }
}