package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.project.Project;

import java.util.List;

import com.intellij.openapi.util.Pair;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetPackageClass;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.Pair;


public final class PluginPackage {

    public static final String getJB_ACCOUNT_FOR_CODE_GOLF_KEY() {
        return CodeGolfConfigurableAccessor. getJB_ACCOUNT_FOR_CODE_GOLF_KEY();
    }

    public static final String getServerUrl() {
        return CodeGolfConfigurableAccessor. getServerUrl();
    }

    public static final String getUserName() {
        return CodeGolfConfigurableAccessor. getUserName();
    }

    public static final void setUserName(String username) {
        CodeGolfConfigurableAccessor. setUserName(username);
    }

    public static final String getUserPassword(Project project) {
        return CodeGolfConfigurableAccessor. getUserPassword(project);
    }

    public static final Pair<String, String> showDialogAndLogin(Project project) {
        return PluginPackage.src.LoginWithJBAccount .1403856597.showDialogAndLogin(project);
    }

    public static final List<String> computeTrimmedLines(String $receiver) {
        return PluginPackage.src.ActionsRecorder. - 162639752. computeTrimmedLines($receiver);
    }

}