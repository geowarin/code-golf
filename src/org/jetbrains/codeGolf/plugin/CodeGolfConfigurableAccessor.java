package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

public final class CodeGolfConfigurableAccessor {
    static final String JB_ACCOUNT_FOR_CODE_GOLF_KEY = "JBAccountForCodeGolf";


    public static final String getJB_ACCOUNT_FOR_CODE_GOLF_KEY() {
        return JB_ACCOUNT_FOR_CODE_GOLF_KEY;
    }


    public static final String getServerUrl() {
        Preconditions.checkNotNull(CodeGolfSettings.DEFAULT_SERVER_URL, "CodeGolfSettings", "DEFAULT_SERVER_URL");
        String defaultServerUrl = CodeGolfSettings.DEFAULT_SERVER_URL;
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        if (propertiesComponent == null) throw new NullPointerException();
        Preconditions.checkNotNull(CodeGolfSettings.SERVER_URL_PROPERTY, "CodeGolfSettings", "SERVER_URL_PROPERTY");
        String serverUrl = propertiesComponent.getValue(CodeGolfSettings.SERVER_URL_PROPERTY, defaultServerUrl);
        Preconditions.checkNotNull(serverUrl, "PropertiesComponent", "getValue");
        return serverUrl;
    }


    public static final String getUserName() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        if (propertiesComponent == null) throw new NullPointerException();
        Preconditions.checkNotNull(CodeGolfSettings.USER_NAME_PROPERTY, "CodeGolfSettings", "USER_NAME_PROPERTY");
        Preconditions.checkNotNull(propertiesComponent.getValue(CodeGolfSettings.USER_NAME_PROPERTY, ""), "PropertiesComponent", "getValue");
        return propertiesComponent.getValue(CodeGolfSettings.USER_NAME_PROPERTY, "");
    }

    public static final void setUserName(String username) {
        Preconditions.checkNotNull(username, "setUserName");
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        if (propertiesComponent == null) throw new NullPointerException();
        Preconditions.checkNotNull(CodeGolfSettings.USER_NAME_PROPERTY, "CodeGolfSettings", "USER_NAME_PROPERTY");
        propertiesComponent.setValue(CodeGolfSettings.USER_NAME_PROPERTY, username);
    }


    public static final String getUserPassword(Project project) {
        Preconditions.checkNotNull(project, "getUserPassword");
        PasswordSafe passwordSafe = PasswordSafe.getInstance();
        if (passwordSafe == null) throw new NullPointerException();
        String userPassword = null;
        try {
            userPassword = passwordSafe.getPassword(project, LoginWithJBAccountAction.class, JB_ACCOUNT_FOR_CODE_GOLF_KEY);
            return userPassword;
        } catch (PasswordSafeException e) {
            throw new RuntimeException(e);
        }
    }
}