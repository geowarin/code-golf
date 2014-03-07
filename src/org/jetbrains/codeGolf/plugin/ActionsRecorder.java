package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.intellij.ide.FrameStateListener;
import com.intellij.ide.IdeEventQueue;
import com.intellij.ide.IdeEventQueue.EventDispatcher;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.impl.IdeKeyEventDispatcher;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.ui.UIUtil;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;

import jet.Function0;
import jet.Function1;
import jet.FunctionImpl0;
import jet.JetObject;
import jet.Unit;

import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;
import org.jetbrains.codeGolf.plugin.log.LogPackage.src.log.767818362;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;


public final class ActionsRecorder implements  Disposable {
    private RecordingControlPanel controlPanel;
    private int movingActionsCounter = 0;
    private int actionsCounter = 0;
    private int typingCounter = 0;
    private ArrayList usedActions;
    private final HashSet actionInputEvents;
    private final Set movingActions;
    private final Set forbiddenActions;
    private final Set typingActions;
    private boolean disposed = false;
    private final GolfTask golfTask;
    private final Project project;
    private final Document document;
    private final String username;
    private String password;
    private final Function0 restarter;


    private final RecordingControlPanel getControlPanel() {
        return this.controlPanel;
    }


    private final void setControlPanel(RecordingControlPanel<set-?>) {
        this.controlPanel =<set - ?>;
    }


    private final int getMovingActionsCounter() {
        return this.movingActionsCounter;
    }


    private final void setMovingActionsCounter(int<set-?>) {
        this.movingActionsCounter =<set - ?>;
    }


    private final int getActionsCounter() {
        return this.actionsCounter;
    }


    private final void setActionsCounter(int<set-?>) {
        this.actionsCounter =<set - ?>;
    }


    private final int getTypingCounter() {
        return this.typingCounter;
    }


    private final void setTypingCounter(int<set-?>) {
        this.typingCounter =<set - ?>;
    }


    private final ArrayList<String> getUsedActions() {
        return this.usedActions;
    }


    private final void setUsedActions(ArrayList<String><set-?>) {
        this.usedActions =<set - ?>;
    }


    private final HashSet<InputEvent> getActionInputEvents() {
        return this.actionInputEvents;
    }


    private final Set<String> getMovingActions() {
        return this.movingActions;
    }


    private final Set<String> getForbiddenActions() {
        return this.forbiddenActions;
    }


    private final Set<String> getTypingActions() {
        return this.typingActions;
    }


    private final boolean getDisposed() {
        return this.disposed;
    }


    private final void setDisposed(boolean<set-?>) {
        this.disposed =<set - ?>;
    }


    public final void startRecording() {
        LogPackage.src.log .767818362.log("recording started");
        this.document
                .addDocumentListener((DocumentListener) new JetObject() {

                    public void beforeDocumentChange(DocumentEvent event) {
                    }

                    public void documentChanged(DocumentEvent event) {
                        if (this.this$0.isTaskSolved()) {
                            Application tmp13_10 = ApplicationManager.getApplication();
                            if (tmp13_10 == null) throw new NullPointerException();
                            tmp13_10.invokeLater((Runnable) new Runnable() {
                                public final void run() {
                                    if (this.this$0.this$0.isTaskSolved())
                                        try {
                                            this.this$0.this$0.sendSolutionToServer();

                                            this.this$0.this$0.stopRecording();
                                        } finally {
                                            this.this$0.this$0.stopRecording();
                                        }

                                }
                            });
                        }
                    }
                }
                        , this);
        ActionManager tmp29_26 = ActionManager.getInstance();
        if (tmp29_26 != null) tmp29_26
                .addAnActionListener((AnActionListener) new JetObject() {

                    public void beforeActionPerformed(AnAction action, Function1<? super String, ? extends Object> dataContext, AnActionEvent event) {
                    }


                    public void afterActionPerformed(AnAction action, Function1<? super String, ? extends Object> dataContext, AnActionEvent event) {
                    }


                    public void beforeEditorTyping(char c, Function1<? super String, ? extends Object> dataContext) {
                    }


                    public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
                        String actionId;
                        ActionManager tmp3_0 = ActionManager.getInstance();
                        if (tmp3_0 != null) {
                            AnAction tmp8_7 = action;
                            if (tmp8_7 == null) throw new NullPointerException();
                            tmpTernaryOp = tmp3_0.getId(tmp8_7);
                        }
                    }


                    public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
                    }


                    public void beforeEditorTyping(char c, DataContext dataContext) {
                    }
                }
                        , this);
    }


    public final boolean isTaskSolved() {
        if (this.disposed) return 0;
        List expected = PluginPackage.src.ActionsRecorder. - 162639752.
        computeTrimmedLines(this.golfTask.getTargetCode());
        List actual = PluginPackage.src.ActionsRecorder. - 162639752.
        computeTrimmedLines(String.valueOf(this.document.getText()));
        LogPackage.src.log .767818362.log("Expected:");
        LogPackage.src.log .767818362.log(expected);
        LogPackage.src.log .767818362.log("Actual:");
        LogPackage.src.log .767818362.log(actual);
        return Objects.equal(expected, actual);
    }

    public final boolean isInsideExpectedCodeViewer(MouseEvent e) {
        Preconditions.checkNotNull(e, "isInsideExpectedCodeViewer");
        Component component = e.getComponent();
        if (component != null) 1;
        if (0 != 0) return 0;
        JRootPane rootPane;
        Window window = SwingUtilities.getWindowAncestor(component);

        return (window instanceof RootPaneContainer) ?
                ((RootPaneContainer) window).getRootPane() : (component instanceof RootPaneContainer) ?
                ((RootPaneContainer) component).getRootPane() :
                0;
    }


    public final void processKeyPressedEvent(KeyEvent e) {
        Preconditions.checkNotNull(e, "processKeyPressedEvent");
        if (this.actionInputEvents.contains(e)) {
            this.actionInputEvents.remove(e);
            return;
        }
        if (KotlinPackage.setOf(new Integer[]{Integer.valueOf(KeyEvent.VK_CONTROL), Integer.valueOf(KeyEvent.VK_ALT), Integer.valueOf(KeyEvent.VK_META), Integer.valueOf(KeyEvent.VK_SHIFT)}).contains(Integer.valueOf(e.getKeyCode())))
            return;
        IdeEventQueue tmp90_87 = IdeEventQueue.getInstance();
        if (tmp90_87 == null) throw new NullPointerException();
        IdeKeyEventDispatcher tmp100_97 = tmp90_87.getKeyEventDispatcher();
        if (tmp100_97 == null) throw new NullPointerException();
        if (!tmp100_97.isReady()) {
            return;
        }

        boolean isChar = (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED ? 0 : 1) != 0 ? UIUtil.isReallyTypedEvent(e) : false;
        boolean hasActionModifiers = !(!e.isAltDown() ? e.isControlDown() : 1) ? e.isMetaDown() : true;
        boolean plainType = isChar ? hasActionModifiers ? 0 : true : false;
        boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

        if ((plainType ? isEnter ? 0 : 1 : 0) != 0) {
            this.usedActions.add(String.valueOf(e.getKeyChar()));
            this.typingCounter += 1;
        } else {
            String tmp270_267 = KeymapUtil.getKeystrokeText(KeyStroke.getKeyStrokeForEvent(e));
            if (tmp270_267 == null) throw new NullPointerException();
            this.usedActions.add(tmp270_267);

            Set movingKeys = KotlinPackage.setOf(new Integer[]{Integer.valueOf(KeyEvent.VK_UP), Integer.valueOf(KeyEvent.VK_DOWN), Integer.valueOf(KeyEvent.VK_LEFT), Integer.valueOf(KeyEvent.VK_RIGHT),
                    Integer.valueOf(KeyEvent.VK_HOME), Integer.valueOf(KeyEvent.VK_END), Integer.valueOf(KeyEvent.VK_PAGE_DOWN), Integer.valueOf(KeyEvent.VK_PAGE_UP)});

            this.movingActionsCounter += 1;

            this.actionsCounter = (((hasActionModifiers ? 0 : 1) != 0 ? movingKeys.contains(Integer.valueOf(e.getKeyCode())) : 0) ?
                    this.movingActionsCounter :
                    this.actionsCounter + 1);
        }

        notifyUser();
    }


    public final void notifyUser() {
        RecordingControlPanel tmp4_1 = this.controlPanel;
        if (tmp4_1 != null) tmp4_1.notifyUser(this.actionsCounter, this.movingActionsCounter, this.typingCounter);
    }


    public final void sendSolutionToServer() {
        GolfSolution solution = new GolfSolution(this.golfTask.getTaskId(), this.username, this.movingActionsCounter, this.typingCounter,
                this.actionsCounter, KotlinPackage.makeString$default((Iterable) this.usedActions, "|", null, null, 0, null, 30));
        String passwordToSend = this.password;
        new Task.Backgroundable(solution, passwordToSend) {

            public void run(ProgressIndicator indicator) {
                Preconditions.checkNotNull(indicator, "run");
                try {
                    GolfResult tmp18_15 = RestClientUtil.sendSolution(this.$solution, this.$passwordToSend);
                    if (tmp18_15 == null) throw new NullPointerException();
                    GolfResult result = tmp18_15;
                    this.this$0.showCongratulations(result);
                } catch (Exception localException) {
                    localException.printStackTrace();
                    Notification notification = new Notification("Code Golf Error", "Cannot upload solution", "Cannot upload solution: " + localException.getMessage(), NotificationType.ERROR);
                    Notifications.Bus.notify(notification, this.this$0.getProject());
                }
            }
        }
                .queue();
    }


    public final void discardSolution(String reason) {
        Preconditions.checkNotNull(reason, "discardSolution");
        stopRecording();

        Notification notification = new Notification("Code Golf Info", "Solution discarded", reason + "<br/><a href=" + "\"" + "restart" + "\"" + ">Try again</a>",
                NotificationType.WARNING, createNotificationListener());
        Notifications.Bus.notify(notification, this.project);
    }

    public final NotificationListener createNotificationListener() {
        return (NotificationListener) new JetObject() {

            public void hyperlinkUpdate(Notification p0, HyperlinkEvent p1) {
                Preconditions.checkNotNull(p0, "hyperlinkUpdate");
                Preconditions.checkNotNull(p1, "hyperlinkUpdate");
                EventType tmp19_16 = EventType.ACTIVATED;
                Preconditions.checkNotNull(tmp19_16, "EventType", "ACTIVATED");
                if ((Objects.equal(p1.getEventType(), tmp19_16) ? Objects.equal(p1.getDescription(), "restart") : 0)) {
                    p0.expire();
                    this.this$0.getRestarter().invoke();
                } else {
                    NotificationListener tmp72_69 = NotificationListener.URL_OPENING_LISTENER;
                    Preconditions.checkNotNull(tmp72_69, "NotificationListener", "URL_OPENING_LISTENER");
                    tmp72_69.hyperlinkUpdate(p0, p1);
                }
            }
        };
    }


    public final void showCongratulations(GolfResult result) {
        Preconditions.checkNotNull(result, "showCongratulations");
        Integer totalCount = result.getResult();
        Notification notification;
        if (totalCount != null) 1;
        if (0 != 0) {
            new Notification(tmp25_22, "Gode Golf Error", "", NotificationType.ERROR);
            "Failed to submit solution";
        }
    }


    public final void stopRecording() {
        LogPackage.src.log .767818362.log("Recording stopped");
        Disposer.dispose(this);
    }

    public void dispose() {
        this.disposed = 1;
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


    public final void setPassword(String<set-?>) {
        Preconditions.checkNotNull( < set - ?>,"<set-password>");
        this.password =<set - ?>;
    }


    public final Function0<Unit> getRestarter() {
        return this.restarter;
    }

    @JetConstructor
    public ActionsRecorder(GolfTask golfTask, Project project, Document document, String username, String password, Function0<? extends Unit> restarter) {
        this.golfTask = golfTask;
        this.project = project;
        this.document = document;
        this.username = username;
        this.password = password;
        this.restarter = restarter;
        this.usedActions = new ArrayList();
        this.actionInputEvents =
                new HashSet();
        this.movingActions =
                KotlinPackage.setOf(new String[]{
                        "EditorLeft", "EditorRight", "EditorDown", "EditorUp",
                        "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown",
                        "EditorPreviousWord", "EditorNextWord",
                        "EditorScrollUp", "EditorScrollDown",
                        "EditorTextStart", "EditorTextEnd",
                        "EditorDownWithSelection", "EditorUpWithSelection",
                        "EditorRightWithSelection", "EditorLeftWithSelection",
                        "EditorLineStartWithSelection", "EditorLineEndWithSelection",
                        "EditorPageDownWithSelection", "EditorPageUpWithSelection"});
        this.forbiddenActions =
                KotlinPackage.setOf(new String[]{"$Paste", "EditorPaste", "PasteMultiple", "EditorPasteSimple",
                        "PlaybackLastMacro", "PlaySavedMacrosAction"});
        this.typingActions =
                KotlinPackage.setOf(new String[]{"EditorBackSpace"});
    }
}