package org.jetbrains.codeGolf.plugin.controlpanel;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.codeGolf.plugin.recording.ActionsRecorder;


@SuppressWarnings("ComponentNotRegistered")
public final class TryAgainAction extends AnAction {
    private final ActionsRecorder recorder;

    public TryAgainAction(ActionsRecorder recorder) {
        super("Try Again", "Reset the game", AllIcons.Actions.Restart);
        this.recorder = recorder;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        recorder.stopRecording();
        recorder.getRestarter().restart();
    }
}