package org.jetbrains.codeGolf.plugin.recording;

import com.google.common.base.Objects;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.codeGolf.plugin.GolfTask;

import java.util.ArrayList;
import java.util.List;

class TaskSolutionChecker {
    private final GolfTask golfTask;

    public TaskSolutionChecker(GolfTask golfTask) {
        this.golfTask = golfTask;
    }

    private List<String> computeTrimmedLines(String input) {
        String[] lines = StringUtil.splitByLines(input);
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            result.add(line.trim());
        }
        return result;
    }

    public boolean checkSolution(String proposedSolution) {
        List expected = computeTrimmedLines(golfTask.getTargetCode());
        List actual = computeTrimmedLines(proposedSolution);
        return Objects.equal(expected, actual);
    }
}
