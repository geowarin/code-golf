package org.jetbrains.codeGolf.plugin;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;


public final class StopSolvingAction extends AnAction {
    private final ActionsRecorder recorder;

    public void actionPerformed(AnActionEvent e) {
        this.recorder.stopRecording();
    }


    public final ActionsRecorder getRecorder() {
        return this.recorder;
    }

    public StopSolvingAction(ActionsRecorder recorder) {
        super("Stop solving", "Stops the game", AllIcons.Actions.Suspend);
        this.recorder = recorder;
    }
}