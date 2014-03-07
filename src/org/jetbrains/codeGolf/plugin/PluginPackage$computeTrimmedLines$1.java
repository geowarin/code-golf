package org.jetbrains.codeGolf.plugin;

import jet.FunctionImpl1;
import jet.runtime.Intrinsics;
import kotlin.KotlinPackage;

final class PluginPackage$computeTrimmedLines$1 extends FunctionImpl1<? super String, ? extends String> {
    static final 1instance$=tmp3_0;

    public final String invoke(String it) {
        Intrinsics.checkParameterIsNotNull(it, "<anonymous>");
        return KotlinPackage.trim(it);
    }
}