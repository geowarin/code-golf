package org.jetbrains.codeGolf.plugin;

import jet.FunctionImpl1;

import kotlin.KotlinPackage;

final class PluginPackage$computeTrimmedLines$1 extends FunctionImpl1<? super String, ? extends String> {
    static final 1instance$=tmp3_0;

    public final String invoke(String it) {
        Preconditions.checkNotNull(it, "<anonymous>");
        return KotlinPackage.trim(it);
    }
}