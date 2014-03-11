package org.jetbrains.codeGolf.plugin.recording;

public class Score {
    private int movingActionsCounter = 0;
    private int actionsCounter = 0;
    private int typingCounter = 0;

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
}