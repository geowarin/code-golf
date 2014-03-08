package org.jetbrains.codeGolf.plugin;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;


public final class TryAgainAction extends AnAction {
    private final ActionsRecorder recorder;


    public void actionPerformed(AnActionEvent e) {
        this.recorder.stopRecording();
        this.recorder.getRestarter().invoke();
    }


    public final ActionsRecorder getRecorder() {
        return this.recorder;
    }

    public TryAgainAction(ActionsRecorder recorder) {
        super("Try Again", "Reset the game", AllIcons.Actions.Restart);
        this.recorder = recorder;
    }
}