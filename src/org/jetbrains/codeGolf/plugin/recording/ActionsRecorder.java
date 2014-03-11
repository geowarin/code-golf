package org.jetbrains.codeGolf.plugin.recording;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.intellij.ide.IdeEventQueue;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
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
import com.intellij.openapi.editor.event.*;
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
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

    public ActionsRecorder(@NotNull GolfTask golfTask, @NotNull Project project, @NotNull Document document, String username, String password) {
        Preconditions.checkNotNull(project);
        this.golfTask = golfTask;
        this.project = project;
        this.document = document;
        editor = IdeaUtils.getEditor(project, document);
        this.username = username;
        this.password = password;
        controlPanel = new RecordingControlPanel(project, document, golfTask.getTargetCode(), this);
        editorMouseListener = new EditorMouseAdapter() {
            @Override
            public void mouseClicked(EditorMouseEvent e) {
                Notifications.Bus.notify(new Notification("mouse on editor", "Don't use mouse on editor!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
                score.increaseMovingActions(1000);
                notifyUser();
            }
        };
    }
//
//    public void setControlPanel(RecordingControlPanel controlPanel) {
//        this.controlPanel = controlPanel;
//    }

    public final void startRecording() {
        recording = true;
        LOG.info("recording started");
        controlPanel.showHint();

        editor.addEditorMouseListener(editorMouseListener);
        document.addDocumentListener(new RecordingDocumentListener(), this);
        ActionManager.getInstance().addAnActionListener(new RecordingActionListener(), this);
    }

    public final boolean isTaskSolved() {
        if (disposed) return false;
        List expected = computeTrimmedLines(golfTask.getTargetCode());
        List actual = computeTrimmedLines(String.valueOf(document.getText()));
        return Objects.equal(expected, actual);
    }

    public final void processKeyPressedEvent(KeyEvent e) {
        if (isModifier(e.getKeyCode()))
            return;

        if (!isEventQueueReady())
            return;

        boolean isChar = e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && UIUtil.isReallyTypedEvent(e);
        boolean hasActionModifiers = e.isAltDown() || e.isControlDown();
        boolean plainType = isChar && !hasActionModifiers;
        boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

        if (plainType && !isEnter) {
            LOG.info("plain typing");
            usedActions.add(String.valueOf(e.getKeyChar()));
            score.increaseTyping(1);
        } else {
            usedActions.add(KeymapUtil.getKeystrokeText(KeyStroke.getKeyStrokeForEvent(e)));

            if (hasActionModifiers && isMovingKey(e.getKeyCode())) {
                LOG.info("moving action");
                score.increaseMovingActions(1);
            } else {
                LOG.info("action");
                score.increaseActions(1);
            }
        }

        notifyUser();
    }

    private boolean isMovingKey(int keyCode) {
        Set movingKeys = Sets.newHashSet(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                          KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP);
        return movingKeys.contains(keyCode);
    }

    private boolean isEventQueueReady() {
        return IdeEventQueue.getInstance().getKeyEventDispatcher().isReady();
    }

    private boolean isModifier(int keyCode) {
        List<Integer> modifiers = Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_META, KeyEvent.VK_SHIFT);
        if (modifiers.contains(keyCode))
            return true;
        return false;
    }

    public final void notifyUser() {
        if (controlPanel != null)
            controlPanel.notifyUser(score.getActionsCounter(), score.getMovingActionsCounter(), score.getTypingCounter());
    }

    private GolfSolution createGolfSolution(String taskId, Score score, List<String> usedActions) {
        return new GolfSolution(taskId, username, score.getMovingActionsCounter(), score.getTypingCounter(), score.getActionsCounter(), Joiner.on('|').join(usedActions));
    }

    public final void discardSolution(String reason) {
        Preconditions.checkNotNull(reason, "discardSolution");
        stopRecording();

        Notification notification = new Notification("Code Golf Info", "Solution discarded", reason + "<br/><a href=\"restart\">Try again</a>",
                NotificationType.WARNING, createNotificationListener());
        Notifications.Bus.notify(notification, project);
    }

    public final NotificationListener createNotificationListener() {

        return new NotificationListener() {
            public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
                if (Objects.equal(event.getEventType(), EventType.ACTIVATED)
                        && Objects.equal(event.getDescription(), "restart")) {
                    notification.expire();
                    getRestarter().restart();
                } else {
                    NotificationListener.URL_OPENING_LISTENER.hyperlinkUpdate(notification, event);
                }
            }
        };
    }

    public final void showCongratulations(GolfResult result) {
        Preconditions.checkNotNull(result, "showCongratulations");
        String errorMessage = result.getErrorMessage();
        Notification notification;
        if (errorMessage != null) {
            notification = new Notification("Failed to submit solution", "Gode Golf Error", errorMessage, NotificationType.ERROR);
        } else {
            if (result.getResult().equals(result.getBestResult())) {
                String message = String.format("Your score of %d is the best score registered so far", result.getResult());
                notification = new Notification("Best score", "Congratulations", message, NotificationType.INFORMATION);
            } else {
                String message = String.format(
                        "Your result is %d. The best result is %d.<br/>" +
                        "The details about the task can be found here.<br/>" +
                        "<a href=\"restart\">Try again</a> if you want to improve your solution.",
                        result.getResult(), result.getBestResult());
                notification = new Notification("Registered", "Congratulations", message, NotificationType.INFORMATION, createNotificationListener());
            }
        }
        Notifications.Bus.notify(notification, project);
    }

    public final void stopRecording() {
        LOG.info("Recording stopped");
        recording = false;
        editor.removeEditorMouseListener(editorMouseListener);
        Disposer.dispose(this);
    }

    @Override
    public void dispose() {
        disposed = true;
        password = "";
        recording = false;
    }

    public final Restarter getRestarter() {
        return restarter;
    }

    private List<String> computeTrimmedLines(String input) {
        String[] lines = StringUtil.splitByLines(input);
        if (lines == null) throw new NullPointerException();

        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            result.add(line.trim());
        }
        return result;
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

    private class RecordingDocumentListener extends DocumentAdapter {

        @Override
        public void documentChanged(DocumentEvent event) {
            if (isTaskSolved()) {
                trySendSolutionToServer();
            }
        }

        private void trySendSolutionToServer() {
            Application application = ApplicationManager.getApplication();
            application.invokeLater(new Runnable() {
                public final void run() {
                    if (isTaskSolved())
                        try {
                            GolfSolution solution = createGolfSolution(golfTask.getTaskId(), score, usedActions);
                            sendSolutionToServer(solution);
                        } finally {
                            stopRecording();
                        }
                }
            });
        }
    }

    private final void sendSolutionToServer(final GolfSolution solution) {
        new Task.Backgroundable(project, "sending data") {
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    GolfResult golfResult = GolfRestClient.getInstance().sendSolution(solution, password);
                    showCongratulations(golfResult);

                } catch (Exception localException) {
                    localException.printStackTrace();
                    Notification notification = new Notification("Code Golf Error", "Cannot upload solution", "Cannot upload solution: " + localException.getMessage(), NotificationType.ERROR);
                    Notifications.Bus.notify(notification, getProject());
                }
            }
        }.queue();
    }


    private class RecordingActionListener implements AnActionListener {
        @Override
        public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
            ActionManager actionManager = ActionManager.getInstance();
            String actionId = actionManager.getId(action);
            LOG.info("actionId = " + actionId);
            if (actionId != null) {
                AnAction editorAction = actionManager.getAction(actionId);
                LOG.info("editorAction = " + editorAction);

                if (actionController.isMovingAction(actionId)) {
                    LOG.info("move");
                    score.increaseMovingActions(1);
                    notifyUser();
                } else if (actionController.isForbiddenAction(ActionsRecorder.this, actionId)) {
                    LOG.info("Forbidden");
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
                        MouseEvent mouseEvent = (MouseEvent) inputEvent;
                        Notifications.Bus.notify(new Notification("mouse action", "Don't use mouse for actions!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
                        score.increaseActions(1000);
                        notifyUser();
                    }
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

}