package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.util.text.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class ActionsRecorderAccessor {

    public static List<String> computeTrimmedLines(String input) {
        String[] lines = StringUtil.splitByLines(input);
        if (lines == null) throw new NullPointerException();

        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            result.add(line.trim());
        }
        return result;
    }
}