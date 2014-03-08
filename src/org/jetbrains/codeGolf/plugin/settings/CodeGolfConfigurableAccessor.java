package org.jetbrains.codeGolf.plugin.settings;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.login.LoginWithJBAccountAction;

public final class CodeGolfConfigurableAccessor {
    static final String JB_ACCOUNT_FOR_CODE_GOLF_KEY = "JBAccountForCodeGolf";

    public static String getServerUrl() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        return propertiesComponent.getValue(CodeGolfSettings.SERVER_URL_PROPERTY, CodeGolfSettings.DEFAULT_SERVER_URL);
    }

    public static String getUserName() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        return propertiesComponent.getValue(CodeGolfSettings.USER_NAME_PROPERTY, "");
    }

    public static void setUserName(String username) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        propertiesComponent.setValue(CodeGolfSettings.USER_NAME_PROPERTY, username);
    }


    public static String getUserPassword(@NotNull Project project) {
        PasswordSafe passwordSafe = PasswordSafe.getInstance();
        try {
            return passwordSafe.getPassword(project, LoginWithJBAccountAction.class, JB_ACCOUNT_FOR_CODE_GOLF_KEY);
        } catch (PasswordSafeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePassword(Project project, String password) {
        PasswordSafe passwordSafe = PasswordSafe.getInstance();
        try {
            passwordSafe.storePassword(project, LoginWithJBAccountAction.class, JB_ACCOUNT_FOR_CODE_GOLF_KEY, password);
        } catch (PasswordSafeException e) {
            throw new RuntimeException(e);
        }
    }
}