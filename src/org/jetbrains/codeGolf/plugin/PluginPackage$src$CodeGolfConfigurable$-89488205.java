package org.jetbrains.codeGolf.plugin;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

public final class PluginPackage$src$CodeGolfConfigurable$-89488205
        {
static final String JB_ACCOUNT_FOR_CODE_GOLF_KEY="JBAccountForCodeGolf";


public static final String getJB_ACCOUNT_FOR_CODE_GOLF_KEY()
        {
        return JB_ACCOUNT_FOR_CODE_GOLF_KEY;
}


public static final String getServerUrl()
        {
        String tmp3_0=CodeGolfSettings.DEFAULT_SERVER_URL;Preconditions.checkNotNull(tmp3_0,"CodeGolfSettings","DEFAULT_SERVER_URL");String defaultServerUrl=tmp3_0;
PropertiesComponent tmp15_12=PropertiesComponent.getInstance();if(tmp15_12==null)throw new NullPointerException();
String tmp25_22=CodeGolfSettings.SERVER_URL_PROPERTY;Preconditions.checkNotNull(tmp25_22,"CodeGolfSettings","SERVER_URL_PROPERTY");
String tmp37_34=tmp15_12.getValue(tmp25_22,defaultServerUrl);Preconditions.checkNotNull(tmp37_34,"PropertiesComponent","getValue");return tmp37_34;
}


public static final String getUserName()
        {
        PropertiesComponent tmp3_0=PropertiesComponent.getInstance();if(tmp3_0==null)throw new NullPointerException();
String tmp13_10=CodeGolfSettings.USER_NAME_PROPERTY;Preconditions.checkNotNull(tmp13_10,"CodeGolfSettings","USER_NAME_PROPERTY");
String tmp26_23=tmp3_0.getValue(tmp13_10,"");Preconditions.checkNotNull(tmp26_23,"PropertiesComponent","getValue");return tmp26_23;
}

public static final void setUserName(String username){
        Preconditions.checkNotNull(username,"setUserName");
PropertiesComponent tmp9_6=PropertiesComponent.getInstance();if(tmp9_6==null)throw new NullPointerException();
String tmp19_16=CodeGolfSettings.USER_NAME_PROPERTY;Preconditions.checkNotNull(tmp19_16,"CodeGolfSettings","USER_NAME_PROPERTY");tmp9_6.setValue(tmp19_16,username);
}


public static final String getUserPassword(Project project){
        Preconditions.checkNotNull(project,"getUserPassword");
PasswordSafe tmp9_6=PasswordSafe.getInstance();if(tmp9_6==null)throw new NullPointerException();
String tmp25_22=tmp9_6.getPassword(project,LoginWithJBAccountAction.class,JB_ACCOUNT_FOR_CODE_GOLF_KEY);if(tmp25_22!=null)tmpTernaryOp=tmp25_22;
}
        }