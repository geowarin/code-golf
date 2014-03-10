package org.jetbrains.codeGolf.plugin;

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
import com.intellij.openapi.keymap.impl.IdeKeyEventDispatcher;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public final class ActionsRecorder implements Disposable {
    public RecordingControlPanel controlPanel;
    private int movingActionsCounter = 0;
    private int actionsCounter = 0;
    private int typingCounter = 0;
    private List<String> usedActions = new ArrayList<String>();
    private final Set<InputEvent> actionInputEvents = new HashSet<InputEvent>();
    private final Set<String> movingActions = Sets.newHashSet("EditorLeft", "EditorRight", "EditorDown", "EditorUp", "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown", "EditorPreviousWord", "EditorNextWord", "EditorScrollUp", "EditorScrollDown", "EditorTextStart", "EditorTextEnd", "EditorDownWithSelection", "EditorUpWithSelection", "EditorRightWithSelection", "EditorLeftWithSelection", "EditorLineStartWithSelection", "EditorLineEndWithSelection", "EditorPageDownWithSelection", "EditorPageUpWithSelection");
    private final Set<String> forbiddenActions = Sets.newHashSet("$Paste", "EditorPaste", "PasteMultiple", "EditorPasteSimple", "PlaybackLastMacro", "PlaySavedMacrosAction");
    private final Set<String> typingActions = Sets.newHashSet("EditorBackSpace");
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
        this.editor = IdeaUtils.getEditor(project, document);
        this.username = username;
        this.password = password;
        this.controlPanel = new RecordingControlPanel(project, document, golfTask.getTargetCode(), this);
        this.editorMouseListener = new EditorMouseAdapter() {
            @Override
            public void mouseClicked(EditorMouseEvent e) {
                Notifications.Bus.notify(new Notification("mouse on editor", "Don't use mouse on editor!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
                movingActionsCounter += 1000;
                notifyUser();
            }
        };
    }

    public void setControlPanel(RecordingControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public final void startRecording() {
        recording = true;
        LOG.info("recording started");
        controlPanel.showHint();

        editor.addEditorMouseListener(editorMouseListener);
        this.document.addDocumentListener(new DocumentListener() {

            public void beforeDocumentChange(DocumentEvent event) {
            }

            public void documentChanged(DocumentEvent event) {
                if (isTaskSolved()) {
                    Application application = ApplicationManager.getApplication();
                    application.invokeLater(new Runnable() {
                        public final void run() {
                            if (isTaskSolved())
                                try {
                                    sendSolutionToServer();
                                } finally {
                                    stopRecording();
                                }

                        }
                    });
                }
            }
        }, this);
        ActionManager.getInstance().addAnActionListener(new AnActionListener() {

            public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
                ActionManager actionManager = ActionManager.getInstance();
                String actionId = actionManager.getId(action);
                LOG.info("actionId = " + actionId);
                if (actionId != null) {
                    AnAction editorAction = actionManager.getAction(actionId);
                    LOG.info("editorAction = " + editorAction);

                    if (movingActions.contains(actionId)) {
                        LOG.info("move");
                        movingActionsCounter++;
                        notifyUser();
                    } else if (forbiddenActions.contains(actionId)) {
                        LOG.info("Forbidden");
                        discardSolution("Action " + actionId + " is forbidden");
                    } else if (typingActions.contains(actionId)) {
                        LOG.info("typing action");
                        typingCounter++;
                        notifyUser();
                    } else {

                        InputEvent inputEvent = event.getInputEvent();
                        if (inputEvent instanceof KeyEvent) {
                            processKeyPressedEvent((KeyEvent) inputEvent);
                        } else if (inputEvent instanceof MouseEvent) {
                            MouseEvent mouseEvent = (MouseEvent) inputEvent;
                            Notifications.Bus.notify(new Notification("mouse action", "Don't use mouse for actions!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
                            actionsCounter += 1000;
                            notifyUser();
                        }
                    }
                }
            }

            public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
            }

            public void beforeEditorTyping(char c, DataContext dataContext) {
                typingCounter++;
                notifyUser();
            }
        }, this);
    }

    public final boolean isTaskSolved() {
        if (this.disposed) return false;
        List expected = computeTrimmedLines(this.golfTask.getTargetCode());
        List actual = computeTrimmedLines(String.valueOf(this.document.getText()));
        return Objects.equal(expected, actual);
    }

    public final boolean isInsideExpectedCodeViewer(MouseEvent e) {
        Preconditions.checkNotNull(e, "isInsideExpectedCodeViewer");
        Component component = e.getComponent();
        if (component == null)
            return true;
        Window window = SwingUtilities.getWindowAncestor(component);

        return (window instanceof RootPaneContainer && ((RootPaneContainer) window).getRootPane() != null)
                || (component instanceof RootPaneContainer && ((RootPaneContainer) component).getRootPane() != null);

//        return (window instanceof RootPaneContainer) ?
//                ((RootPaneContainer) window).getRootPane() : (component instanceof RootPaneContainer) ?
//                ((RootPaneContainer) component).getRootPane() :
//                false;
    }

    public final void processKeyPressedEvent(KeyEvent e) {
        Preconditions.checkNotNull(e, "processKeyPressedEvent");
        if (this.actionInputEvents.contains(e)) {
            this.actionInputEvents.remove(e);
            return;
        }
        List<Integer> modifiers = Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_META, KeyEvent.VK_SHIFT);
        if (modifiers.contains(Integer.valueOf(e.getKeyCode())))
            return;
        IdeEventQueue eventQueue = IdeEventQueue.getInstance();
        IdeKeyEventDispatcher keyEventDispatcher = eventQueue.getKeyEventDispatcher();
        if (!keyEventDispatcher.isReady())
            return;

        boolean isChar = e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && UIUtil.isReallyTypedEvent(e);
        boolean hasActionModifiers = e.isAltDown() || e.isControlDown();
        boolean plainType = isChar && !hasActionModifiers;
        boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

        if (plainType && !isEnter) {
            LOG.info("plain typing");
            this.usedActions.add(String.valueOf(e.getKeyChar()));
            this.typingCounter++;
        } else {
            String keystrokeText = KeymapUtil.getKeystrokeText(KeyStroke.getKeyStrokeForEvent(e));
            if (keystrokeText == null) throw new NullPointerException();
            this.usedActions.add(keystrokeText);

            Set movingKeys = Sets.newHashSet(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                    KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP);

            if (hasActionModifiers && movingKeys.contains(e.getKeyCode())) {
                LOG.info("moving action");
                this.movingActionsCounter++;
            } else {
                LOG.info("action");
                this.actionsCounter++;
            }
        }

        notifyUser();
    }

    public final void notifyUser() {
        if (this.controlPanel != null)
            this.controlPanel.notifyUser(this.actionsCounter, this.movingActionsCounter, this.typingCounter);
    }

    public final void sendSolutionToServer() {
        final GolfSolution solution =
                new GolfSolution(this.golfTask.getTaskId(), this.username, this.movingActionsCounter, this.typingCounter,
                        this.actionsCounter, Joiner.on('|').join(this.usedActions));
//                        KotlinPackage.makeString$default((Iterable) this.usedActions, "|", null, null, 0, null, 30));
        final String passwordToSend = this.password;

        new Task.Backgroundable(project, "sending data") {

            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    GolfResult golfResult = RestClientUtil.sendSolution(solution, passwordToSend);
                    showCongratulations(golfResult);

                } catch (Exception localException) {
                    localException.printStackTrace();
                    Notification notification = new Notification("Code Golf Error", "Cannot upload solution", "Cannot upload solution: " + localException.getMessage(), NotificationType.ERROR);
                    Notifications.Bus.notify(notification, this.getProject());
                }
            }
        }.queue();
    }

    public final void discardSolution(String reason) {
        Preconditions.checkNotNull(reason, "discardSolution");
        stopRecording();

        Notification notification = new Notification("Code Golf Info", "Solution discarded", reason + "<br/><a href=\"restart\">Try again</a>",
                NotificationType.WARNING, createNotificationListener());
        Notifications.Bus.notify(notification, this.project);
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
        Notifications.Bus.notify(notification, this.project);
    }

    public final void stopRecording() {
        LOG.info("Recording stopped");
        this.recording = false;
        this.editor.removeEditorMouseListener(editorMouseListener);
        Disposer.dispose(this);
    }

    public void dispose() {
        this.disposed = true;
        this.password = "";
        this.recording = false;
    }

    public final Restarter getRestarter() {
        return this.restarter;
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
}