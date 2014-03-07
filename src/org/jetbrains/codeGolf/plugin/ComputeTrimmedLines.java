package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import jet.FunctionImpl1;

import kotlin.KotlinPackage;

final class ComputeTrimmedLines {
    public static final String invoke(String it) {
        Preconditions.checkNotNull(it, "<anonymous>");
        return it.trim();
    }
}