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
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
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
    private RecordingControlPanel controlPanel;
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
    private final Restarter restarter;

    private static final Logger LOG = Logger.getInstance(ActionsRecorder.class.getName());

    public ActionsRecorder(GolfTask golfTask, Project project, Document document, String username, String password, Restarter restarter) {
        this.golfTask = golfTask;
        this.project = project;
        this.document = document;
        this.username = username;
        this.password = password;
        this.restarter = restarter;
    }


    public void setControlPanel(RecordingControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public final void startRecording() {
        LOG.info("recording started");

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
        ActionManager actionManager = ActionManager.getInstance();
        if (actionManager != null) actionManager.addAnActionListener(new AnActionListener() {

            public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
                ActionManager actionManager = ActionManager.getInstance();
                String actionId = actionManager.getId(action);
                if (actionId != null) {
//                    AnAction editorAction = actionManager.getAction(actionId);

                    if (movingActions.contains(actionId)) {
                        movingActionsCounter++;
                        notifyUser();
                    } else if (forbiddenActions.contains(actionId)) {
                        discardSolution("Action " + actionId + " is forbidden");
                    } else if (typingActions.contains(actionId)) {
                        typingCounter++;
                        notifyUser();
                    } else {
                        KeyEvent keyEvent = IdeEventQueue.getInstance().getKeyEventDispatcher().getContext().getInputEvent();
                        processKeyPressedEvent(keyEvent);
                    }
                }

            }

            public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
                System.out.println("after");
            }

            public void beforeEditorTyping(char c, DataContext dataContext) {
                actionsCounter++;
                notifyUser();
            }
        }, this);
    }


    public final boolean isTaskSolved() {
        if (this.disposed) return false;
        List expected = computeTrimmedLines(this.golfTask.getTargetCode());
        List actual = computeTrimmedLines(String.valueOf(this.document.getText()));
        LOG.info("Expected:");
        LOG.info(expected.toString());
        LOG.info("Actual:");
        LOG.info(actual.toString());
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
            this.usedActions.add(String.valueOf(e.getKeyChar()));
            this.typingCounter += 1;
        } else {
            String keystrokeText = KeymapUtil.getKeystrokeText(KeyStroke.getKeyStrokeForEvent(e));
            if (keystrokeText == null) throw new NullPointerException();
            this.usedActions.add(keystrokeText);

            Set movingKeys = Sets.newHashSet(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                    KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP);

            this.movingActionsCounter += 1;

            this.actionsCounter =
                    !hasActionModifiers && movingKeys.contains(Integer.valueOf(e.getKeyCode())) ?
                            this.movingActionsCounter : this.actionsCounter + 1;
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
                        this.actionsCounter,
                        Joiner.on('|').join(this.usedActions));
//                        KotlinPackage.makeString$default((Iterable) this.usedActions, "|", null, null, 0, null, 30));
        final String passwordToSend = this.password;

        new Task.Backgroundable(project, passwordToSend) {

            public void run(@NotNull ProgressIndicator indicator) {
                Preconditions.checkNotNull(indicator, "run");
                try {
                    GolfResult golfResult = RestClientUtil.sendSolution(solution, passwordToSend);
                    if (golfResult == null) throw new NullPointerException();
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

        Notification notification = new Notification("Code Golf Info", "Solution discarded", reason + "<br/><a href=" + "\"" + "restart" + "\"" + ">Try again</a>",
                NotificationType.WARNING, createNotificationListener());
        Notifications.Bus.notify(notification, this.project);
    }

    public final NotificationListener createNotificationListener() {

        return new NotificationListener() {
            public void hyperlinkUpdate(Notification notification, HyperlinkEvent event) {
                Preconditions.checkNotNull(notification, "hyperlinkUpdate");
                Preconditions.checkNotNull(event, "hyperlinkUpdate");
                if ((Objects.equal(event.getEventType(), EventType.ACTIVATED)
                        && Objects.equal(event.getDescription(), "restart"))) {
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
        if (errorMessage != null) {
            new Notification("Failed to submit solution", "Gode Golf Error", "", NotificationType.ERROR);
            return;
        }

        if (result.getResult().equals(result.getBestResult())) {

        }
    }

    public final void stopRecording() {
        LOG.info("Recording stopped");
        Disposer.dispose(this);
    }

    public void dispose() {
        this.disposed = true;
        this.password = "";
    }


    public final GolfTask getGolfTask() {
        return this.golfTask;
    }


    public final Project getProject() {
        return this.project;
    }


    public final Document getDocument() {
        return this.document;
    }


    public final String getUsername() {
        return this.username;
    }


    public final String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public interface Restarter {
        public void restart();
    }
}