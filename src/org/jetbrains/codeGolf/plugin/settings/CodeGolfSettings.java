package org.jetbrains.codeGolf.plugin.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.util.SystemProperties;

public class CodeGolfSettings {
    public static final String DEFAULT_SERVER_URL = "http://code-golf.appspot.com";
    public static final String SERVER_URL_PROPERTY = "CODE_GOLF_SERVER_URL";
    public static final String USER_NAME_PROPERTY = "CODE_GOLF_USER_NAME";

    public static String getServerUrl() {
        return PropertiesComponent.getInstance().getValue("CODE_GOLF_SERVER_URL", "http://code-golf.appspot.com");
    }

    public static String getUserName() {
        String systemUserName = SystemProperties.getUserName();
        String defaultUserName = systemUserName != null ? systemUserName : "user";
        return PropertiesComponent.getInstance().getValue("CODE_GOLF_USER_NAME", defaultUserName);
    }
}