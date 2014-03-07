package org.jetbrains.codeGolf.plugin;

import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;

public final class PluginPackage$src$GolfTask$1895599820
{
  @JetMethod(flags=16, returnType="Ljava/lang/String;")
  public static final String toHtml(@JetValueParameter(name="$receiver", receiver=true, type="Lorg/jetbrains/codeGolf/plugin/GolfTask;") GolfTask $receiver)
  {
    return KotlinPackage.replaceAll(KotlinPackage.replaceAll($receiver.getTargetCode(), "\n", "<BR/>"), " ", "&nbsp;");
  }
}