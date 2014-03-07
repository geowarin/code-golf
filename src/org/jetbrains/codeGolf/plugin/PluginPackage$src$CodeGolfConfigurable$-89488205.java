package org.jetbrains.codeGolf.plugin;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

public final class PluginPackage$src$CodeGolfConfigurable$-89488205
{
  static final String JB_ACCOUNT_FOR_CODE_GOLF_KEY = "JBAccountForCodeGolf";

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public static final String getJB_ACCOUNT_FOR_CODE_GOLF_KEY()
  {
    return JB_ACCOUNT_FOR_CODE_GOLF_KEY;
  }

  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String getServerUrl()
  {
    String tmp3_0 = CodeGolfSettings.DEFAULT_SERVER_URL; Intrinsics.checkFieldIsNotNull(tmp3_0, "CodeGolfSettings", "DEFAULT_SERVER_URL"); String defaultServerUrl = tmp3_0;
    PropertiesComponent tmp15_12 = PropertiesComponent.getInstance(); if (tmp15_12 == null) Intrinsics.throwNpe();
    String tmp25_22 = CodeGolfSettings.SERVER_URL_PROPERTY; Intrinsics.checkFieldIsNotNull(tmp25_22, "CodeGolfSettings", "SERVER_URL_PROPERTY");
    String tmp37_34 = tmp15_12.getValue(tmp25_22, defaultServerUrl); Intrinsics.checkReturnedValueIsNotNull(tmp37_34, "PropertiesComponent", "getValue"); return tmp37_34;
  }

  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String getUserName()
  {
    PropertiesComponent tmp3_0 = PropertiesComponent.getInstance(); if (tmp3_0 == null) Intrinsics.throwNpe();
    String tmp13_10 = CodeGolfSettings.USER_NAME_PROPERTY; Intrinsics.checkFieldIsNotNull(tmp13_10, "CodeGolfSettings", "USER_NAME_PROPERTY");
    String tmp26_23 = tmp3_0.getValue(tmp13_10, ""); Intrinsics.checkReturnedValueIsNotNull(tmp26_23, "PropertiesComponent", "getValue"); return tmp26_23;
  }
  @JetMethod(flags=16, returnType="V")
  public static final void setUserName(@JetValueParameter(name="username", type="Ljava/lang/String;") String username) {
    Intrinsics.checkParameterIsNotNull(username, "setUserName");
    PropertiesComponent tmp9_6 = PropertiesComponent.getInstance(); if (tmp9_6 == null) Intrinsics.throwNpe();
    String tmp19_16 = CodeGolfSettings.USER_NAME_PROPERTY; Intrinsics.checkFieldIsNotNull(tmp19_16, "CodeGolfSettings", "USER_NAME_PROPERTY"); tmp9_6.setValue(tmp19_16, username);
  }

  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String getUserPassword(@JetValueParameter(name="project", type="Lcom/intellij/openapi/project/Project;") Project project) {
    Intrinsics.checkParameterIsNotNull(project, "getUserPassword");
    PasswordSafe tmp9_6 = PasswordSafe.getInstance(); if (tmp9_6 == null) Intrinsics.throwNpe();
    String tmp25_22 = tmp9_6.getPassword(project, LoginWithJBAccountAction.class, JB_ACCOUNT_FOR_CODE_GOLF_KEY); if (tmp25_22 != null) tmpTernaryOp = tmp25_22;
  }
}