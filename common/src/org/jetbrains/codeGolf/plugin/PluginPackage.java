package org.jetbrains.codeGolf.plugin;

import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetPackageClass;
import jet.runtime.typeinfo.JetValueParameter;

@JetPackageClass(abiVersion = 6)
public final class PluginPackage {

    @JetMethod(flags = 16, returnType = "Ljava/lang/String;")
    public static final String toHtml(@JetValueParameter(name = "$receiver", receiver = true, type = "Lorg/jetbrains/codeGolf/plugin/GolfTask;") GolfTask $receiver) {
        return PluginPackage.src.GolfTask .1895599820.toHtml($receiver);
    }
}