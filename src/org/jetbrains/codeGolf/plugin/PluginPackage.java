package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.project.Project;
import java.util.List;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetPackageClass;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.Pair;

@JetPackageClass(abiVersion=6)
public final class PluginPackage
{
  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public static final String getJB_ACCOUNT_FOR_CODE_GOLF_KEY()
  {
    return PluginPackage.src.CodeGolfConfigurable.-89488205.getJB_ACCOUNT_FOR_CODE_GOLF_KEY(); } 
  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String getServerUrl() { return PluginPackage.src.CodeGolfConfigurable.-89488205.getServerUrl(); } 
  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String getUserName() { return PluginPackage.src.CodeGolfConfigurable.-89488205.getUserName(); } 
  @JetMethod(flags=16, returnType="V")
  public static final void setUserName(@JetValueParameter(name="username", type="Ljava/lang/String;") String username) { PluginPackage.src.CodeGolfConfigurable.-89488205.setUserName(username); } 
  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String getUserPassword(@JetValueParameter(name="project", type="Lcom/intellij/openapi/project/Project;") Project project) { return PluginPackage.src.CodeGolfConfigurable.-89488205.getUserPassword(project); } 
  @JetMethod(flags=16, returnType="?Lkotlin/Pair<Ljava/lang/String;Ljava/lang/String;>;")
  public static final Pair<String, String> showDialogAndLogin(@JetValueParameter(name="project", type="Lcom/intellij/openapi/project/Project;") Project project) { return PluginPackage.src.LoginWithJBAccount.1403856597.showDialogAndLogin(project); } 
  @JetMethod(flags=16, returnType="Ljet/List<Ljava/lang/String;>;")
  public static final List<String> computeTrimmedLines(@JetValueParameter(name="$receiver", receiver=true, type="Ljava/lang/String;") String $receiver) { return PluginPackage.src.ActionsRecorder.-162639752.computeTrimmedLines($receiver); }

}