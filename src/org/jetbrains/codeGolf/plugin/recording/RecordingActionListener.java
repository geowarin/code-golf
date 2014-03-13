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
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Date: 12/03/2014
 * Time: 23:15
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
class RecordingActionListener implements AnActionListener {
    private final Score score;
    private final ActionController actionController;
    private final RecordingControlPanel controlPanel;
    private final ActionsRecorder actionsRecorder;

    private static final Logger LOG = Logger.getInstance("#org.jetbrains.codeGolf");


    RecordingActionListener(Score score, RecordingControlPanel controlPanel, ActionsRecorder actionsRecorder) {
        this.score = score;
        this.controlPanel = controlPanel;
        this.actionsRecorder = actionsRecorder;
        actionController = new ActionController();
    }

    @Override
    public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        String actionId = ActionManager.getInstance().getId(action);
        if (actionId == null) {
            return;
        }
        if (actionController.isForbiddenAction(actionId)) {
            LOG.info("Forbidden " + actionId);
            actionsRecorder.discardSolution("Action " + actionId + " is forbidden");
            return;
        }
        InputEvent inputEvent = event.getInputEvent();
        if (inputEvent instanceof MouseEvent) {
            processMouseAction();
            return;
        }

        KeyEvent keyEvent = (KeyEvent) inputEvent;
        if (actionController.isMovingAction(actionId)) {
            LOG.debug("move " + actionId);
            score.increaseMovingActions(1);
            score.addKeyStroke("move", KeyStroke.getKeyStrokeForEvent(keyEvent));
            controlPanel.notifyUser(score);
            return;
        }

        if (actionController.isTypingAction(actionId)) {
            score.addKeyStroke("typing", KeyStroke.getKeyStrokeForEvent(keyEvent));
            score.increaseTyping(1);
            controlPanel.notifyUser(score);
            return;
        }

        processKeyPressedEvent((KeyEvent) inputEvent);
    }

    @Override
    public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
    }

    @Override
    public void beforeEditorTyping(char c, DataContext dataContext) {
        score.addCharacterTyped(c);
        score.increaseTyping(1);
        controlPanel.notifyUser(score);
    }

    private void processMouseAction() {
        Notifications.Bus.notify(new Notification("mouse action", "Don't use mouse for actions!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
        score.increaseActions(1000);
        controlPanel.notifyUser(score);
    }

    private void processKeyPressedEvent(KeyEvent event) {
        if (actionController.isModifier(event.getKeyCode()))
            return;

        if (!isEventQueueReady())
            return;

        boolean isChar = event.getKeyChar() != KeyEvent.CHAR_UNDEFINED && UIUtil.isReallyTypedEvent(event);
        boolean hasActionModifiers = event.isAltDown() || event.isControlDown() || event.isMetaDown();
        boolean plainType = isChar && !hasActionModifiers;
        boolean isEnter = event.getKeyCode() == KeyEvent.VK_ENTER;

        if (plainType && !isEnter) {
            score.addCharacterTyped(event.getKeyChar());
            score.increaseTyping(1);
            return;
        }


        if (hasActionModifiers && actionController.isMovingKey(event.getKeyCode())) {
            score.increaseMovingActions(1);
            score.addKeyStroke("moving action", KeyStroke.getKeyStrokeForEvent(event));
        } else {
            score.increaseActions(1);
            score.addKeyStroke("action", KeyStroke.getKeyStrokeForEvent(event));
        }
        controlPanel.notifyUser(score);
    }

    private boolean isEventQueueReady() {
        return IdeEventQueue.getInstance().getKeyEventDispatcher().isReady();
    }
}
