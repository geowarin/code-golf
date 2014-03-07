package org.jetbrains.codeGolf.plugin;

import com.intellij.ide.FrameStateListener;
import com.intellij.ide.IdeEventQueue;
import com.intellij.ide.IdeEventQueue.EventDispatcher;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
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
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;
import org.jetbrains.codeGolf.plugin.log.LogPackage.src.log.767818362;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;

@JetClass(signature="Ljava/lang/Object;Lcom/intellij/openapi/Disposable;", abiVersion=6)
public final class ActionsRecorder
  implements JetObject, Disposable
{
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

  @JetMethod(flags=9, propertyType="?Lorg/jetbrains/codeGolf/plugin/RecordingControlPanel;")
  private final RecordingControlPanel getControlPanel()
  {
    return this.controlPanel;
  }

  @JetMethod(flags=9, propertyType="?Lorg/jetbrains/codeGolf/plugin/RecordingControlPanel;")
  private final void setControlPanel(@JetValueParameter(name="<set-?>", type="?Lorg/jetbrains/codeGolf/plugin/RecordingControlPanel;") RecordingControlPanel <set-?>)
  {
    this.controlPanel = <set-?>;
  }

  @JetMethod(flags=9, propertyType="I")
  private final int getMovingActionsCounter()
  {
    return this.movingActionsCounter;
  }

  @JetMethod(flags=9, propertyType="I")
  private final void setMovingActionsCounter(@JetValueParameter(name="<set-?>", type="I") int <set-?>)
  {
    this.movingActionsCounter = <set-?>;
  }

  @JetMethod(flags=9, propertyType="I")
  private final int getActionsCounter()
  {
    return this.actionsCounter;
  }

  @JetMethod(flags=9, propertyType="I")
  private final void setActionsCounter(@JetValueParameter(name="<set-?>", type="I") int <set-?>)
  {
    this.actionsCounter = <set-?>;
  }

  @JetMethod(flags=9, propertyType="I")
  private final int getTypingCounter()
  {
    return this.typingCounter;
  }

  @JetMethod(flags=9, propertyType="I")
  private final void setTypingCounter(@JetValueParameter(name="<set-?>", type="I") int <set-?>)
  {
    this.typingCounter = <set-?>;
  }

  @JetMethod(flags=9, propertyType="Ljava/util/ArrayList<Ljava/lang/String;>;")
  private final ArrayList<String> getUsedActions()
  {
    return this.usedActions;
  }

  @JetMethod(flags=9, propertyType="Ljava/util/ArrayList<Ljava/lang/String;>;")
  private final void setUsedActions(@JetValueParameter(name="<set-?>", type="Ljava/util/ArrayList<Ljava/lang/String;>;") ArrayList<String> <set-?>)
  {
    this.usedActions = <set-?>;
  }

  @JetMethod(flags=9, propertyType="Ljava/util/HashSet<Ljava/awt/event/InputEvent;>;")
  private final HashSet<InputEvent> getActionInputEvents()
  {
    return this.actionInputEvents;
  }

  @JetMethod(flags=9, propertyType="Ljet/Set<Ljava/lang/String;>;")
  private final Set<String> getMovingActions()
  {
    return this.movingActions;
  }

  @JetMethod(flags=9, propertyType="Ljet/Set<Ljava/lang/String;>;")
  private final Set<String> getForbiddenActions()
  {
    return this.forbiddenActions;
  }

  @JetMethod(flags=9, propertyType="Ljet/Set<Ljava/lang/String;>;")
  private final Set<String> getTypingActions()
  {
    return this.typingActions;
  }

  @JetMethod(flags=9, propertyType="Z")
  private final boolean getDisposed()
  {
    return this.disposed;
  }

  @JetMethod(flags=9, propertyType="Z")
  private final void setDisposed(@JetValueParameter(name="<set-?>", type="Z") boolean <set-?>)
  {
    this.disposed = <set-?>;
  }

  @JetMethod(returnType="V")
  public final void startRecording()
  {
    LogPackage.src.log.767818362.log("recording started");
    this.document
      .addDocumentListener((DocumentListener)new JetObject() {
      @JetMethod(returnType="V")
      public void beforeDocumentChange(@JetValueParameter(name="event", type="?Lcom/intellij/openapi/editor/event/DocumentEvent;") DocumentEvent event) {  } 
      @JetMethod(returnType="V")
      public void documentChanged(@JetValueParameter(name="event", type="?Lcom/intellij/openapi/editor/event/DocumentEvent;") DocumentEvent event) { if (this.this$0.isTaskSolved())
        {
          Application tmp13_10 = ApplicationManager.getApplication(); if (tmp13_10 == null) Intrinsics.throwNpe(); tmp13_10.invokeLater((Runnable)new Runnable() {
            public final void run() {
              if (this.this$0.this$0.isTaskSolved())
                try {
                  this.this$0.this$0.sendSolutionToServer();

                  this.this$0.this$0.stopRecording(); } finally { this.this$0.this$0.stopRecording(); }

            }
          });
        }
      }
    }
    , this);
    ActionManager tmp29_26 = ActionManager.getInstance(); if (tmp29_26 != null) tmp29_26
        .addAnActionListener((AnActionListener)new JetObject()
      {
        @JetMethod(returnType="V")
        public void beforeActionPerformed(@JetValueParameter(name="action", type="?Lcom/intellij/openapi/actionSystem/AnAction;") AnAction action, @JetValueParameter(name="dataContext", type="?Ljet/Function1<?Ljava/lang/String;?Ljava/lang/Object;>;") Function1<? super String, ? extends Object> dataContext, @JetValueParameter(name="event", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent event)
        {
        }

        @JetMethod(returnType="V")
        public void afterActionPerformed(@JetValueParameter(name="action", type="?Lcom/intellij/openapi/actionSystem/AnAction;") AnAction action, @JetValueParameter(name="dataContext", type="?Ljet/Function1<?Ljava/lang/String;?Ljava/lang/Object;>;") Function1<? super String, ? extends Object> dataContext, @JetValueParameter(name="event", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent event)
        {
        }

        @JetMethod(returnType="V")
        public void beforeEditorTyping(@JetValueParameter(name="c", type="C") char c, @JetValueParameter(name="dataContext", type="?Ljet/Function1<?Ljava/lang/String;?Ljava/lang/Object;>;") Function1<? super String, ? extends Object> dataContext)
        {
        }

        @JetMethod(returnType="V")
        public void beforeActionPerformed(@JetValueParameter(name="action", type="?Lcom/intellij/openapi/actionSystem/AnAction;") AnAction action, @JetValueParameter(name="dataContext", type="?Lcom/intellij/openapi/actionSystem/DataContext;") DataContext dataContext, @JetValueParameter(name="event", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent event)
        {
          String actionId;
          ActionManager tmp3_0 = ActionManager.getInstance(); if (tmp3_0 != null)
          {
            AnAction tmp8_7 = action; if (tmp8_7 == null) Intrinsics.throwNpe(); tmpTernaryOp = tmp3_0.getId(tmp8_7);
          }
        }

        @JetMethod(returnType="V")
        public void afterActionPerformed(@JetValueParameter(name="action", type="?Lcom/intellij/openapi/actionSystem/AnAction;") AnAction action, @JetValueParameter(name="dataContext", type="?Lcom/intellij/openapi/actionSystem/DataContext;") DataContext dataContext, @JetValueParameter(name="event", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent event)
        {
        }

        @JetMethod(returnType="V")
        public void beforeEditorTyping(@JetValueParameter(name="c", type="C") char c, @JetValueParameter(name="dataContext", type="?Lcom/intellij/openapi/actionSystem/DataContext;") DataContext dataContext)
        {
        }
      }
      , this);
  }

  @JetMethod(flags=16, returnType="Z")
  public final boolean isTaskSolved()
  {
    if (this.disposed) return 0;
    List expected = PluginPackage.src.ActionsRecorder.-162639752.computeTrimmedLines(this.golfTask.getTargetCode());
    List actual = PluginPackage.src.ActionsRecorder.-162639752.computeTrimmedLines(String.valueOf(this.document.getText()));
    LogPackage.src.log.767818362.log("Expected:");
    LogPackage.src.log.767818362.log(expected);
    LogPackage.src.log.767818362.log("Actual:");
    LogPackage.src.log.767818362.log(actual);
    return Intrinsics.areEqual(expected, actual);
  }
  @JetMethod(flags=16, returnType="Z")
  public final boolean isInsideExpectedCodeViewer(@JetValueParameter(name="e", type="Ljava/awt/event/MouseEvent;") MouseEvent e) {
    Intrinsics.checkParameterIsNotNull(e, "isInsideExpectedCodeViewer"); Component component = e.getComponent();
    if (component != null) 1; if (0 != 0) return 0;
    JRootPane rootPane;
    Window window = SwingUtilities.getWindowAncestor(component);

    return (window instanceof RootPaneContainer) ? 
      ((RootPaneContainer)window).getRootPane() : (component instanceof RootPaneContainer) ? 
      ((RootPaneContainer)component).getRootPane() : 
      0;
  }

  @JetMethod(flags=16, returnType="V")
  public final void processKeyPressedEvent(@JetValueParameter(name="e", type="Ljava/awt/event/KeyEvent;") KeyEvent e)
  {
    Intrinsics.checkParameterIsNotNull(e, "processKeyPressedEvent"); if (this.actionInputEvents.contains(e)) {
      this.actionInputEvents.remove(e);
      return;
    }
    if (KotlinPackage.setOf(new Integer[] { Integer.valueOf(KeyEvent.VK_CONTROL), Integer.valueOf(KeyEvent.VK_ALT), Integer.valueOf(KeyEvent.VK_META), Integer.valueOf(KeyEvent.VK_SHIFT) }).contains(Integer.valueOf(e.getKeyCode())))
      return;
    IdeEventQueue tmp90_87 = IdeEventQueue.getInstance(); if (tmp90_87 == null) Intrinsics.throwNpe();
    IdeKeyEventDispatcher tmp100_97 = tmp90_87.getKeyEventDispatcher(); if (tmp100_97 == null) Intrinsics.throwNpe(); if (!tmp100_97.isReady()) {
      return;
    }

    boolean isChar = (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED ? 0 : 1) != 0 ? UIUtil.isReallyTypedEvent(e) : false;
    boolean hasActionModifiers = !(!e.isAltDown() ? e.isControlDown() : 1) ? e.isMetaDown() : true;
    boolean plainType = isChar ? hasActionModifiers ? 0 : true : false;
    boolean isEnter = e.getKeyCode() == KeyEvent.VK_ENTER;

    if ((plainType ? isEnter ? 0 : 1 : 0) != 0) {
      this.usedActions.add(String.valueOf(e.getKeyChar()));
      this.typingCounter += 1;
    }
    else
    {
      String tmp270_267 = KeymapUtil.getKeystrokeText(KeyStroke.getKeyStrokeForEvent(e)); if (tmp270_267 == null) Intrinsics.throwNpe(); this.usedActions.add(tmp270_267);

      Set movingKeys = KotlinPackage.setOf(new Integer[] { Integer.valueOf(KeyEvent.VK_UP), Integer.valueOf(KeyEvent.VK_DOWN), Integer.valueOf(KeyEvent.VK_LEFT), Integer.valueOf(KeyEvent.VK_RIGHT), 
        Integer.valueOf(KeyEvent.VK_HOME), Integer.valueOf(KeyEvent.VK_END), Integer.valueOf(KeyEvent.VK_PAGE_DOWN), Integer.valueOf(KeyEvent.VK_PAGE_UP) });

      this.movingActionsCounter += 1;

      this.actionsCounter = (((hasActionModifiers ? 0 : 1) != 0 ? movingKeys.contains(Integer.valueOf(e.getKeyCode())) : 0) ? 
        this.movingActionsCounter : 
        this.actionsCounter + 1);
    }

    notifyUser();
  }

  @JetMethod(returnType="V")
  public final void notifyUser()
  {
    RecordingControlPanel tmp4_1 = this.controlPanel; if (tmp4_1 != null) tmp4_1.notifyUser(this.actionsCounter, this.movingActionsCounter, this.typingCounter); 
  }

  @JetMethod(flags=16, returnType="V")
  public final void sendSolutionToServer()
  {
    GolfSolution solution = new GolfSolution(this.golfTask.getTaskId(), this.username, this.movingActionsCounter, this.typingCounter, 
      this.actionsCounter, KotlinPackage.makeString$default((Iterable)this.usedActions, "|", null, null, 0, null, 30));
    String passwordToSend = this.password;
    new Task.Backgroundable(solution, passwordToSend) {
      @JetMethod(returnType="V")
      public void run(@JetValueParameter(name="indicator", type="Lcom/intellij/openapi/progress/ProgressIndicator;") ProgressIndicator indicator) { Intrinsics.checkParameterIsNotNull(indicator, "run");
        try
        {
          GolfResult tmp18_15 = RestClientUtil.sendSolution(this.$solution, this.$passwordToSend); if (tmp18_15 == null) Intrinsics.throwNpe(); GolfResult result = tmp18_15;
          this.this$0.showCongratulations(result);
        }
        catch (Exception localException) {
          localException.printStackTrace();
          Notification notification = new Notification("Code Golf Error", "Cannot upload solution", "Cannot upload solution: " + localException.getMessage(), NotificationType.ERROR);
          Notifications.Bus.notify(notification, this.this$0.getProject());
        }
      }
    }
    .queue();
  }

  @JetMethod(flags=16, returnType="V")
  public final void discardSolution(@JetValueParameter(name="reason", type="Ljava/lang/String;") String reason)
  {
    Intrinsics.checkParameterIsNotNull(reason, "discardSolution"); stopRecording();

    Notification notification = new Notification("Code Golf Info", "Solution discarded", reason + "<br/><a href=" + "\"" + "restart" + "\"" + ">Try again</a>", 
      NotificationType.WARNING, createNotificationListener());
    Notifications.Bus.notify(notification, this.project);
  }
  @JetMethod(flags=16, returnType="Lcom/intellij/notification/NotificationListener;")
  public final NotificationListener createNotificationListener() { return (NotificationListener)new JetObject() {
      @JetMethod(returnType="V")
      public void hyperlinkUpdate(@JetValueParameter(name="p0", type="Lcom/intellij/notification/Notification;") Notification p0, @JetValueParameter(name="p1", type="Ljavax/swing/event/HyperlinkEvent;") HyperlinkEvent p1) { Intrinsics.checkParameterIsNotNull(p0, "hyperlinkUpdate"); Intrinsics.checkParameterIsNotNull(p1, "hyperlinkUpdate");
        EventType tmp19_16 = EventType.ACTIVATED; Intrinsics.checkFieldIsNotNull(tmp19_16, "EventType", "ACTIVATED"); if ((Intrinsics.areEqual(p1.getEventType(), tmp19_16) ? Intrinsics.areEqual(p1.getDescription(), "restart") : 0)) {
          p0.expire();
          this.this$0.getRestarter().invoke();
        }
        else
        {
          NotificationListener tmp72_69 = NotificationListener.URL_OPENING_LISTENER; Intrinsics.checkFieldIsNotNull(tmp72_69, "NotificationListener", "URL_OPENING_LISTENER"); tmp72_69.hyperlinkUpdate(p0, p1);
        } }
    }; }

  @JetMethod(returnType="V")
  public final void showCongratulations(@JetValueParameter(name="result", type="Lorg/jetbrains/codeGolf/plugin/GolfResult;") GolfResult result) {
    Intrinsics.checkParameterIsNotNull(result, "showCongratulations"); Integer totalCount = result.getResult();
    Notification notification;
    if (totalCount != null) 1; if (0 != 0) {
      new Notification(tmp25_22, "Gode Golf Error", "", NotificationType.ERROR); "Failed to submit solution";
    }
  }

  @JetMethod(returnType="V")
  public final void stopRecording()
  {
    LogPackage.src.log.767818362.log("Recording stopped");
    Disposer.dispose(this);
  }
  @JetMethod(returnType="V")
  public void dispose() {
    this.disposed = 1;
    this.password = "";
  }

  @JetMethod(flags=17, propertyType="Lorg/jetbrains/codeGolf/plugin/GolfTask;")
  public final GolfTask getGolfTask()
  {
    return this.golfTask;
  }

  @JetMethod(flags=17, propertyType="Lcom/intellij/openapi/project/Project;")
  public final Project getProject()
  {
    return this.project;
  }

  @JetMethod(flags=17, propertyType="Lcom/intellij/openapi/editor/Document;")
  public final Document getDocument()
  {
    return this.document;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getUsername()
  {
    return this.username;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getPassword()
  {
    return this.password;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setPassword(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-password>");
    this.password = <set-?>;
  }

  @JetMethod(flags=1, propertyType="Ljet/Function0<Ljet/Unit;>;")
  public final Function0<Unit> getRestarter()
  {
    return this.restarter;
  }

  @JetConstructor
  public ActionsRecorder(@JetValueParameter(name="golfTask", type="Lorg/jetbrains/codeGolf/plugin/GolfTask;") GolfTask golfTask, @JetValueParameter(name="project", type="Lcom/intellij/openapi/project/Project;") Project project, @JetValueParameter(name="document", type="Lcom/intellij/openapi/editor/Document;") Document document, @JetValueParameter(name="username", type="Ljava/lang/String;") String username, @JetValueParameter(name="password", type="Ljava/lang/String;") String password, @JetValueParameter(name="restarter", type="Ljet/Function0<Ljet/Unit;>;") Function0<? extends Unit> restarter)
  {
    this.golfTask = golfTask; this.project = project; this.document = document; this.username = username; this.password = password; this.restarter = restarter; this.usedActions = new ArrayList(); this.actionInputEvents = 
      new HashSet(); this.movingActions = 
      KotlinPackage.setOf(new String[] { 
      "EditorLeft", "EditorRight", "EditorDown", "EditorUp", 
      "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown", 
      "EditorPreviousWord", "EditorNextWord", 
      "EditorScrollUp", "EditorScrollDown", 
      "EditorTextStart", "EditorTextEnd", 
      "EditorDownWithSelection", "EditorUpWithSelection", 
      "EditorRightWithSelection", "EditorLeftWithSelection", 
      "EditorLineStartWithSelection", "EditorLineEndWithSelection", 
      "EditorPageDownWithSelection", "EditorPageUpWithSelection" }); this.forbiddenActions = 
      KotlinPackage.setOf(new String[] { "$Paste", "EditorPaste", "PasteMultiple", "EditorPasteSimple", 
      "PlaybackLastMacro", "PlaySavedMacrosAction" }); this.typingActions = 
      KotlinPackage.setOf(new String[] { "EditorBackSpace" });
  }
}