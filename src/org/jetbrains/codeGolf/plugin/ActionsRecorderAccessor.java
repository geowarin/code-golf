package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.util.text.StringUtil;
import jet.Function1;
import kotlin.KotlinPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ActionsRecorderAccessor {

    public static final List<String> computeTrimmedLines(String $receiver) {
        String[] tmp4_1 = StringUtil.splitByLines($receiver);
        if (tmp4_1 == null) throw new NullPointerException();
        return (List) KotlinPackage.mapTo((Object[]) tmp4_1, (Collection) new ArrayList(), (Function1) PluginPackage.computeTrimmedLines
        .1.instance$);
    }
}