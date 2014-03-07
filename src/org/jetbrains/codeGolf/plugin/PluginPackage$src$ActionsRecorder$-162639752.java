package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.util.text.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jet.Function1;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;

public final class PluginPackage$src$ActionsRecorder$-162639752
{
  @JetMethod(flags=16, returnType="Ljet/List<Ljava/lang/String;>;")
  public static final List<String> computeTrimmedLines(@JetValueParameter(name="$receiver", receiver=true, type="Ljava/lang/String;") String $receiver)
  {
    String[] tmp4_1 = StringUtil.splitByLines($receiver); if (tmp4_1 == null) Intrinsics.throwNpe(); return (List)KotlinPackage.mapTo((Object[])tmp4_1, (Collection)new ArrayList(), (Function1)PluginPackage.computeTrimmedLines.1.instance$);
  }
}