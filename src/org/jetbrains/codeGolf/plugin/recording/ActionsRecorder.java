package org.jetbrains.codeGolf.plugin.recording;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.intellij.ide.IdeEventQueue;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.EditorMouseAdapter;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfResult;
import org.jetbrains.codeGolf.plugin.GolfSolution;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.IdeaUtils;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.task.GolfRestClient;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public final class ActionsRecorder implements Disposable {
    private final Score score = new Score();
    private final ActionController actionController = new ActionController();
    private final RecordingControlPanel controlPanel;
    private final List<String> usedActions = new ArrayList<String>();
    private boolean disposed = false;
    private final GolfTask golfTask;
    private final Project project;
    private final Document document;
    private final String username;
    private String password;
    private Restarter restarter;
    private boolean recording;

    private static final Logger LOG = Logger.getInstance("#org.jetbrains.codeGolf");
    private final Editor editor;
    private final EditorMouseAdapter editorMouseListener;
    private Notifier notifier;

    public ActionsRecorder(@NotNull GolfTask golfTask, @NotNull Project project, @NotNull Document document, String username, String password) {
        this.golfTask = golfTask;
        this.project = project;
        this.document = document;
        editor = IdeaUtils.getEditor(project, document);
        this.username = username;
        this.password = password;
        controlPanel = new RecordingControlPanel(project, document, golfTask.getTargetCode(), this);
        editorMouseListener = new PenaliseMouseOnEditorListener();
    }

    public final void startRecording() {
        recording = true;
        LOG.info("recording started");
        controlPanel.showHint();

        notifier = new Notifier(project, restarter);
        editor.addEditorMouseListener(editorMouseListener);
        document.addDocumentListener(new SolutionFoundDocumentListener(), this);
        ActionManager.getInstance().addAnActionListener(new RecordingActionListener(), this);
    }

    public final void stopRecording() {
        LOG.info("Recording stopped");
        recording = false;
        editor.removeEditorMouseListener(editorMouseListener);
        Disposer.dispose(this);
    }

    public final void processKeyPressedEvent(KeyEvent e) {
        if (actionController.isModifier(e.getKeyCode()))
            return;

        if (!isEventQueueReady())
            return;

        boolean isChar = e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && UIUtil.isReallyTypedEvent(e);
        boolean hasActionModifiers = e.isAltDown() || e.isControlDown();
        boolean plainType = isChar && !hasActionModifiers;
        boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

        if (plainType && !isEnter) {
            usedActions.add(String.valueOf(e.getKeyChar()));
            LOG.info("plain typing " + String.valueOf(e.getKeyChar()));
            score.increaseTyping(1);
        } else {
            String keystrokeText = KeymapUtil.getKeystrokeText(KeyStroke.getKeyStrokeForEvent(e));
            usedActions.add(keystrokeText);

            if (hasActionModifiers && actionController.isMovingKey(e.getKeyCode())) {
                LOG.info("moving action " + keystrokeText);
                score.increaseMovingActions(1);
            } else {
                LOG.info("action " + keystrokeText);
                score.increaseActions(1);
            }
        }
        notifyUser();
    }

    private boolean isEventQueueReady() {
        return IdeEventQueue.getInstance().getKeyEventDispatcher().isReady();
    }

    public final void notifyUser() {
        controlPanel.notifyUser(score.getActionsCounter(), score.getMovingActionsCounter(), score.getTypingCounter());
    }

    public final void discardSolution(String reason) {
        stopRecording();
        notifier.notifySolutionDiscarded(reason);
    }

    public final Restarter getRestarter() {
        return restarter;
    }

    public void setRestarter(Restarter restarter) {
        this.restarter = restarter;
    }

    public interface Restarter {
        public void restart();
    }

    public boolean isRecording() {
        return recording;
    }

    private class RecordingActionListener implements AnActionListener {
        @Override
        public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
            ActionManager actionManager = ActionManager.getInstance();
            String actionId = actionManager.getId(action);
            if (actionId == null) {
                return;
            }
            AnAction editorAction = actionManager.getAction(actionId);
            LOG.info("actionId = " + actionId);
            LOG.info("editorAction = " + editorAction);

            if (actionController.isMovingAction(actionId)) {
                LOG.info("move " + actionId);
                score.increaseMovingActions(1);
                notifyUser();
            } else if (actionController.isForbiddenAction(ActionsRecorder.this, actionId)) {
                LOG.info("Forbidden " + actionId);
                discardSolution("Action " + actionId + " is forbidden");
            } else if (actionController.isTypingAction(actionId)) {
                LOG.info("typing action");
                score.increaseTyping(1);
                notifyUser();
            } else {
                InputEvent inputEvent = event.getInputEvent();
                if (inputEvent instanceof KeyEvent) {
                    processKeyPressedEvent((KeyEvent) inputEvent);
                } else if (inputEvent instanceof MouseEvent) {
                    processMouseAction();
                }
            }
        }

        @Override
        public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        }

        @Override
        public void beforeEditorTyping(char c, DataContext dataContext) {
            score.increaseTyping(1);
            notifyUser();
        }
    }

    private void processMouseAction() {
        Notifications.Bus.notify(new Notification("mouse action", "Don't use mouse for actions!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
        score.increaseActions(1000);
        notifyUser();
    }

    private class SolutionFoundDocumentListener extends DocumentAdapter {
        @Override
        public void documentChanged(DocumentEvent event) {
            if (isTaskSolved())
                sendSolutionWhenPossible(createGolfSolution(golfTask.getTaskId(), score, usedActions));
        }

        private void sendSolutionWhenPossible(final GolfSolution solution) {
            Application application = ApplicationManager.getApplication();
            application.invokeLater(new Runnable() {
                public final void run() {
                    if (isTaskSolved()) {
                        try {
                            trySendSolutionToServer(solution);
                        } finally { // Dispose must be done in dispatch thread
                            stopRecording();
                        }
                    }
                }
            });
        }
    }

    private final void trySendSolutionToServer(final GolfSolution solution) {
        new Task.Backgroundable(project, "sending data") {
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    sendSolution(solution);
                } catch (Exception exception) {
                    notifier.notifyUploadError(exception.getMessage());
                }
            }
        }.queue();
    }

    private GolfSolution createGolfSolution(String taskId, Score score, List<String> usedActions) {
        return new GolfSolution(taskId, username, score.getMovingActionsCounter(), score.getTypingCounter(), score.getActionsCounter(), Joiner.on('|').join(usedActions));
    }

    private void sendSolution(GolfSolution solution) {
        GolfResult golfResult = GolfRestClient.getInstance().sendSolution(solution, password);
        if (golfResult.getErrorMessage() != null) {
            notifier.showSubmitError(golfResult.getErrorMessage());
        } else {
            notifier.showCongratulations(golfResult);
        }
    }

    public final boolean isTaskSolved() {
        if (disposed) return false;
        List expected = computeTrimmedLines(golfTask.getTargetCode());
        List actual = computeTrimmedLines(String.valueOf(document.getText()));
        return Objects.equal(expected, actual);
    }

    private static List<String> computeTrimmedLines(String input) {
        String[] lines = StringUtil.splitByLines(input);
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            result.add(line.trim());
        }
        return result;
    }

    @Override
    public void dispose() {
        disposed = true;
        password = "";
        recording = false;
    }

    private class PenaliseMouseOnEditorListener extends EditorMouseAdapter {
        @Override
        public void mouseClicked(EditorMouseEvent e) {
            notifyMouseUsedInEditor();
            score.increaseMovingActions(1000);
            notifyUser();
        }
    }

    private void notifyMouseUsedInEditor() {
        Notifications.Bus.notify(new Notification("mouse on editor", "Don't use mouse on editor!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
    }
}