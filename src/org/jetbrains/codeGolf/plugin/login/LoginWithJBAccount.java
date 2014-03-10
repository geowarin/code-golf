package org.jetbrains.codeGolf.plugin.login;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.auth.JBAccountAuthHelper;
import org.jetbrains.codeGolf.plugin.settings.CodeGolfConfigurableAccessor;

public final class LoginWithJBAccount extends LoginService {

    @Override
    public Credentials showDialogAndLogin(@NotNull Project project) {
        String username = CodeGolfConfigurableAccessor.getUserName();
        String password = CodeGolfConfigurableAccessor.getUserPassword(project);

        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            JBAccountDialog dlg = new JBAccountDialog(project);
            dlg.show();
            if (!dlg.isOK()) {
                return null;
            }
            username = dlg.getUsername();
            password = dlg.getPassword();
        }
        String encodedPassword = JBAccountAuthHelper.encodePassword(password);
        return new JBCredentials(username, password, encodedPassword);
    }
}