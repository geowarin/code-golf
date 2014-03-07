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
import com.intellij.util.ui.UIUtil;
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
    private List<String> usedActions;
    private final Set<InputEvent> actionInputEvents;
    private final Set<String> movingActions;
    private final Set<String> forbiddenActions;
    private final Set<String> typingActions;
    private boolean disposed = false;
    private final GolfTask golfTask;
    private final Project project;
    private final Document document;
    private final String username;
    private String password;
    private final Restarter restarter;

    private static final Logger LOG = Logger.getInstance(ActionsRecorder.class.getName());

    public void setControlPanel(RecordingControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    private int getMovingActionsCounter() {
        return this.movingActionsCounter;
    }


    public void setMovingActionsCounter(int movingActionsCounter) {
        this.movingActionsCounter = movingActionsCounter;
    }

    public int getActionsCounter() {
        return actionsCounter;
    }

    public void setActionsCounter(int actionsCounter) {
        this.actionsCounter = actionsCounter;
    }

    public int getTypingCounter() {
        return typingCounter;
    }

    public void setTypingCounter(int typingCounter) {
        this.typingCounter = typingCounter;
    }

    private List<String> getUsedActions() {
        return this.usedActions;
    }

    public void setUsedActions(List<String> usedActions) {
        this.usedActions = usedActions;
    }

    private Set<InputEvent> getActionInputEvents() {
        return this.actionInputEvents;
    }


    private Set<String> getMovingActions() {
        return this.movingActions;
    }


    private Set<String> getForbiddenActions() {
        return this.forbiddenActions;
    }


    private Set<String> getTypingActions() {
        return this.typingActions;
    }


    private boolean getDisposed() {
        return this.disposed;
    }

    public void setDisposed(boolean disposed) {
        this.disposed = disposed;
    }

    public final void startRecording() {
        LOG.info("recording started");

        final ActionsRecorder recorder = this;
        this.document.addDocumentListener(new DocumentListener() {

            public void beforeDocumentChange(DocumentEvent event) {
            }

            public void documentChanged(DocumentEvent event) {
                if (recorder.isTaskSolved()) {
                    Application application = ApplicationManager.getApplication();
                    application.invokeLater(new Runnable() {
                        public final void run() {
                            if (recorder.isTaskSolved())
                                try {
                                    recorder.sendSolutionToServer();
                                } finally {
                                    recorder.stopRecording();
                                }

                        }
                    });
                }
            }
        }, this);
        ActionManager actionManager = ActionManager.getInstance();
        if (actionManager != null) actionManager.addAnActionListener(new AnActionListener() {

            public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
                String actionId;
                ActionManager actionManager = ActionManager.getInstance();
                if (actionManager != null) {
                    if (action == null) throw new NullPointerException();
                    final String id = actionManager.getId(action);
                    // TODO ??
                }
            }

            public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
            }

            public void beforeEditorTyping(char c, DataContext dataContext) {
            }
        }, this);
    }


    public final boolean isTaskSolved() {
        if (this.disposed) return false;
        List expected = ActionsRecorderAccessor.computeTrimmedLines(this.golfTask.getTargetCode());
        List actual = ActionsRecorderAccessor.computeTrimmedLines(String.valueOf(this.document.getText()));
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
        List<Integer> keys = Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_META, KeyEvent.VK_SHIFT);
        if (keys.contains(Integer.valueOf(e.getKeyCode())))
            return;
        IdeEventQueue eventQueue = IdeEventQueue.getInstance();
        if (eventQueue == null) throw new NullPointerException();
        IdeKeyEventDispatcher keyEventDispatcher = eventQueue.getKeyEventDispatcher();
        if (keyEventDispatcher == null) throw new NullPointerException();
        if (!keyEventDispatcher.isReady()) {
            return;
        }

        boolean isChar = e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && UIUtil.isReallyTypedEvent(e);
        boolean hasActionModifiers = e.isAltDown() || e.isControlDown();
        boolean plainType = isChar && !hasActionModifiers;
        boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

        if ((plainType ? isEnter ? 0 : 1 : 0) != 0) {
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
        RecordingControlPanel tmp4_1 = this.controlPanel;
        if (tmp4_1 != null) tmp4_1.notifyUser(this.actionsCounter, this.movingActionsCounter, this.typingCounter);
    }


    public final void sendSolutionToServer() {
        final GolfSolution solution =
                new GolfSolution(this.golfTask.getTaskId(), this.username, this.movingActionsCounter, this.typingCounter,
                        this.actionsCounter,
                        Joiner.on('|').join(this.usedActions));
//                        KotlinPackage.makeString$default((Iterable) this.usedActions, "|", null, null, 0, null, 30));
        final String passwordToSend = this.password;

        final ActionsRecorder recorder = this;
        new Task.Backgroundable(project, passwordToSend) {

            public void run(ProgressIndicator indicator) {
                Preconditions.checkNotNull(indicator, "run");
                try {
                    GolfResult golfResult = RestClientUtil.sendSolution(solution, passwordToSend);
                    if (golfResult == null) throw new NullPointerException();
                    GolfResult result = golfResult;
                    recorder.showCongratulations(result);

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

        final ActionsRecorder recorder = this;
        return new NotificationListener() {
            public void hyperlinkUpdate(Notification notification, HyperlinkEvent event) {
                Preconditions.checkNotNull(notification, "hyperlinkUpdate");
                Preconditions.checkNotNull(event, "hyperlinkUpdate");
                if ((Objects.equal(event.getEventType(), EventType.ACTIVATED)
                        && Objects.equal(event.getDescription(), "restart"))) {
                    notification.expire();
                    recorder.getRestarter().invoke();
                } else {
                    NotificationListener tmp72_69 = NotificationListener.URL_OPENING_LISTENER;
                    Preconditions.checkNotNull(tmp72_69, "NotificationListener", "URL_OPENING_LISTENER");
                    tmp72_69.hyperlinkUpdate(notification, event);
                }
            }
        };
    }


    public final void showCongratulations(GolfResult result) {
        Preconditions.checkNotNull(result, "showCongratulations");
        Integer totalCount = result.getResult();
        if (totalCount == null) {
            new Notification("Failed to submit solution", "Gode Golf Error", "", NotificationType.ERROR);
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

    public ActionsRecorder(GolfTask golfTask, Project project, Document document, String username, String password, Restarter restarter) {
        this.golfTask = golfTask;
        this.project = project;
        this.document = document;
        this.username = username;
        this.password = password;
        this.restarter = restarter;
        this.usedActions = new ArrayList<String>();
        this.actionInputEvents = new HashSet<InputEvent>();
        this.movingActions = Sets.newHashSet("EditorLeft", "EditorRight", "EditorDown", "EditorUp", "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown",
                "EditorPreviousWord", "EditorNextWord", "EditorScrollUp", "EditorScrollDown", "EditorTextStart", "EditorTextEnd", "EditorDownWithSelection", "EditorUpWithSelection",
                "EditorRightWithSelection", "EditorLeftWithSelection", "EditorLineStartWithSelection", "EditorLineEndWithSelection", "EditorPageDownWithSelection", "EditorPageUpWithSelection");
        this.forbiddenActions = Sets.newHashSet("$Paste", "EditorPaste", "PasteMultiple", "EditorPasteSimple", "PlaybackLastMacro", "PlaySavedMacrosAction");
        this.typingActions = Sets.newHashSet("EditorBackSpace");
    }

    public interface Restarter {
        public void invoke();
    }
}