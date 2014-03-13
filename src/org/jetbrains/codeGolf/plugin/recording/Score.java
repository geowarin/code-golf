package org.jetbrains.codeGolf.plugin.recording;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.KeymapUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Score {
    private int movingActionsCounter;
    private int actionsCounter;
    private int typingCounter;
    private final List<String> usedActions = new ArrayList<String>();
    private static final Logger LOG = Logger.getInstance("#org.jetbrains.codeGolf");

    public int getMovingActionsCounter() {
        return movingActionsCounter;
    }

    public void increaseMovingActions(int number) {
        movingActionsCounter += number;
    }

    public int getActionsCounter() {
        return actionsCounter;
    }

    public void increaseActions(int number) {
        actionsCounter += number;
    }

    public int getTypingCounter() {
        return typingCounter;
    }

    public void increaseTyping(int number) {
        typingCounter += number;
    }

    public void addChar(char keyChar) {
        usedActions.add(String.valueOf(keyChar));
        LOG.info("char : " + keyChar);
    }

    public void addKeyStroke(String type, KeyStroke keyStrokeForEvent) {
        String keystrokeText = KeymapUtil.getKeystrokeText(keyStrokeForEvent);
        usedActions.add(keystrokeText);
        LOG.info("action " + type + " : " + keystrokeText + "(" + keyStrokeForEvent + ")");
    }

    public List<String> getUsedActions() {
        return usedActions;
    }
}