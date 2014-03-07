package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;

final class ComputeTrimmedLines {
    public static final String invoke(String it) {
        Preconditions.checkNotNull(it, "<anonymous>");
        return it.trim();
    }
}