package org.jetbrains.codeGolf.plugin.controlpanel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.codeGolf.plugin.ActionsRecorder;


@SuppressWarnings("ComponentNotRegistered")
public final class StopSolvingAction extends AnAction {
    private final ActionsRecorder recorder;

    public StopSolvingAction(ActionsRecorder recorder) {
        super("Stop solving", "Stops the game", AllIcons.Actions.Suspend);
        this.recorder = recorder;
    }

    public void actionPerformed(AnActionEvent e) {
        this.recorder.stopRecording();
    }
}