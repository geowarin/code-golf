package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.codeGolf.plugin.event.Events;
import org.jetbrains.codeGolf.plugin.event.StartGameEvent;
import org.jetbrains.codeGolf.plugin.game.Game;


public final class StartGolfAction extends AnAction {

    public StartGolfAction() {
        super("Start Code Golf...");
    }

    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(!isGameStarted());
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        if (!isGameStarted()) {
            Events.post(new StartGameEvent(anActionEvent.getProject()));
        }
    }

    private boolean isGameStarted() {
        return Game.get().isStarted();
    }

}