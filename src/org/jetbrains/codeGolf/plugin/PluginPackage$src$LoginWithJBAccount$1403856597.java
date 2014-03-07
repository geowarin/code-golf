package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;

import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.Pair;
import org.jetbrains.codeGolf.auth.JBAccountAuthHelper;

public final class PluginPackage$src$LoginWithJBAccount$1403856597 {

    public static final Pair<String, String> showDialogAndLogin(Project project) {
        Preconditions.checkNotNull(project, "showDialogAndLogin");
        String username = PluginPackage.src.CodeGolfConfigurable. - 89488205. getUserName();
        String password = PluginPackage.src.CodeGolfConfigurable. - 89488205. getUserPassword(project);
        if ((!StringUtil.isEmpty(username) ? StringUtil.isEmpty(password) : 1)) {
            JBAccountDialog dlg = new JBAccountDialog(project);
            dlg.show();
            if (!dlg.isOK()) {
                return (Pair) null;
            }
            username = dlg.getUsername();
            password = dlg.getPassword();
        }
        String tmp77_74 = JBAccountAuthHelper.encodePassword(password);
        if (tmp77_74 == null) throw new NullPointerException();
        return new Pair(username, tmp77_74);
    }
}