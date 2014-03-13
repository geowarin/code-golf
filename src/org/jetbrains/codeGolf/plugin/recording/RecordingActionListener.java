package org.jetbrains.codeGolf.plugin.recording;

import com.intellij.ide.IdeEventQueue;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

class RecordingActionListener implements AnActionListener {
    private final ActionController actionController;
    private final Score score;
    private final ActionsRecorder recorder;
    private static final Logger LOG = Logger.getInstance("#org.jetbrains.codeGolf");

    RecordingActionListener(ActionController actionController, Score score, ActionsRecorder recorder) {
        this.actionController = actionController;
        this.score = score;
        this.recorder = recorder;
    }

    @Override
    public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        ActionManager actionManager = ActionManager.getInstance();
        String actionId = actionManager.getId(action);
        if (actionId == null) {
            return;
        }
        if (actionController.isForbiddenAction(actionId)) {
            LOG.info("Forbidden " + actionId);
            recorder.discardSolution("Action " + actionId + " is forbidden");
            return;
        }

        InputEvent inputEvent = event.getInputEvent();
        if (inputEvent instanceof MouseEvent) {
            processMouseAction();
            return;
        }
        KeyEvent keyEvent = (KeyEvent) inputEvent;

//        AnAction editorAction = actionManager.getAction(actionId);
        if (actionController.isMovingAction(actionId)) {
            score.increaseMovingActions(1);
            score.addKeyStroke("move", KeyStroke.getKeyStrokeForEvent(keyEvent));
            recorder.notifyUser();
            return;
        }
        if (actionController.isTypingAction(actionId)) {
            score.increaseTyping(1);
            score.addKeyStroke("typing", KeyStroke.getKeyStrokeForEvent(keyEvent));
            recorder.notifyUser();
            return;
        }

        processKeyPressedEvent((KeyEvent) inputEvent);
    }

    @Override
    public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
    }

    @Override
    public void beforeEditorTyping(char c, DataContext dataContext) {
        score.addChar(c);
        score.increaseTyping(1);
        recorder.notifyUser();
    }

    private void processMouseAction() {
        Notifications.Bus.notify(new Notification("mouse action", "Don't use mouse for actions!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
        score.increaseActions(1000);
        recorder.notifyUser();
    }

    private void processKeyPressedEvent(KeyEvent e) {
        if (actionController.isModifier(e.getKeyCode()))
            return;

        if (!isEventQueueReady())
            return;

        boolean isChar = e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && UIUtil.isReallyTypedEvent(e);
        boolean hasActionModifiers = e.isAltDown() || e.isControlDown();
        boolean plainType = isChar && !hasActionModifiers;
        boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

        if (plainType && !isEnter) {
            score.addChar(e.getKeyChar());
            score.increaseTyping(1);
        } else {
            score.addKeyStroke("action", KeyStroke.getKeyStrokeForEvent(e));

            if (hasActionModifiers && actionController.isMovingKey(e.getKeyCode())) {
                score.increaseMovingActions(1);
            } else {
                score.increaseActions(1);
            }
        }
        recorder.notifyUser();
    }

    private boolean isEventQueueReady() {
        return IdeEventQueue.getInstance().getKeyEventDispatcher().isReady();
    }

}
