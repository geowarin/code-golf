package org.jetbrains.codeGolf.plugin.login;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 10/03/2014
 * Time: 21:31
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public abstract class LoginService {

    public static LoginService getInstance() {
        return ServiceManager.getService(LoginService.class);
    }


    public abstract Credentials showDialogAndLogin(@NotNull Project project);
}
