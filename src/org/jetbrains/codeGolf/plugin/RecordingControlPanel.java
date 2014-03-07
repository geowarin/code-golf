package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.ui.popup.NotLookupOrSearchCondition;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import jet.ExtensionFunction0;
import jet.ExtensionFunctionImpl0;
import jet.JetObject;
import jet.TypeCastException;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;


public final class RecordingControlPanel extends JPanel
  implements JetObject, Disposable
{
  private Editor myEditor;
  private final JLabel myText;
  private final JBPopup myHint;
  private final int MAX_WIDTH;
  private final int MAX_HEIGHT;
  private final int MIN_HEIGHT;
  private final Project project;
  private final Document document;
  private final String targetCode;
  private final ActionsRecorder recorder;


  private final Editor getMyEditor()
  {
    return this.myEditor;
  }


  private final void setMyEditor( Editor <set-?>)
  {
    this.myEditor = <set-?>;
  }


  private final JLabel getMyText()
  {
    return this.myText;
  }


  public final JBPopup getMyHint()
  {
    return this.myHint;
  }


  private final int getMAX_WIDTH()
  {
    return this.MAX_WIDTH;
  }


  private final int getMAX_HEIGHT()
  {
    return this.MAX_HEIGHT;
  }


  private final int getMIN_HEIGHT()
  {
    return this.MIN_HEIGHT;
  }


  public void dispose()
  {
    if (this.myHint == null) 1; if ((0 != 0 ? this.myHint.isDisposed() ? 0 : 1 : 0) != 0) {
      this.myHint.cancel();
    }
    if (this.myEditor == null) 1; if (0 != 0)
    {
      EditorFactory tmp62_59 = EditorFactory.getInstance(); if (tmp62_59 == null) throw new NullPointerException();
      Editor tmp73_70 = this.myEditor; if (tmp73_70 == null) throw new NullPointerException(); tmp62_59.releaseEditor(tmp73_70);
      this.myEditor = ((Editor)null);
    }
  }


  public final JComponent createControlComponent()
  {
    DefaultActionGroup group = new DefaultActionGroup(new AnAction[] { 
      (AnAction)new ShowDiffWithExpectedAction(this.targetCode, this.document), 
      (AnAction)new NavigateToEditorAction(this.document), 
      (AnAction)Separator.getInstance(), 
      (AnAction)new TryAgainAction(this.recorder), 
      (AnAction)new StopSolvingAction(this.recorder) });
    ActionManager tmp96_93 = ActionManager.getInstance(); if (tmp96_93 == null) throw new NullPointerException();
    ActionToolbar tmp114_111 = tmp96_93.createActionToolbar("CodeGolfToolbar", (ActionGroup)group, 1); if (tmp114_111 == null) throw new NullPointerException();
    JComponent tmp126_121 = tmp114_111.getComponent(); if (tmp126_121 == null) throw new NullPointerException(); return tmp126_121;
  }

  public final void notifyUser( int actionsCounter,  int movingActionsCounter,  int typingCounter) {
    notifyUser("<html>Actions: <b>" + actionsCounter + "</b>.&nbsp; Moving actions: <b>" + movingActionsCounter + "</b>.&nbsp; Chars: <b>" + typingCounter + "</b>.</html>");
  }

  public final void notifyUser( String message) {
    Intrinsics.checkParameterIsNotNull(message, "notifyUser"); this.myText.setText(message);
    this.myText.revalidate();
    this.myText.repaint();
  }

  public final void showHint() {
    JBPopup hint = this.myHint;
    if (hint != null) 1; if (0 != 0)
      return;
    WindowManager tmp19_16 = WindowManager.getInstance(); if (tmp19_16 == null) throw new NullPointerException();
    IdeFrame tmp33_30 = tmp19_16.getIdeFrame(this.project); if (tmp33_30 == null) throw new NullPointerException();
    JComponent tmp45_40 = tmp33_30.getComponent(); if (tmp45_40 == null) throw new NullPointerException(); JComponent frame = tmp45_40;
    Rectangle tmp57_54 = frame.getVisibleRect(); Intrinsics.checkReturnedValueIsNotNull(tmp57_54, "JComponent", "getVisibleRect"); Rectangle visibleRect = tmp57_54;
    Dimension tmp70_67 = getPreferredSize(); if (tmp70_67 == null) throw new NullPointerException(); Dimension contentSize = tmp70_67;
    JBPopup tmp85_84 = hint; if (tmp85_84 == null) throw new TypeCastException("com.intellij.openapi.ui.popup.JBPopup? cannot be cast to com.intellij.ui.popup.AbstractPopup");
    Dimension tmp105_102 = ((AbstractPopup)tmp85_84).getHeaderPreferredSize(); Intrinsics.checkReturnedValueIsNotNull(tmp105_102, "AbstractPopup", "getHeaderPreferredSize"); int popupHeight = contentSize.height + tmp105_102.height;
    int newPopupHeight = Math.max(Math.min(popupHeight, visibleRect.height / 2), 150);
    if (newPopupHeight != popupHeight) {
      popupHeight = newPopupHeight;
      ((AbstractPopup)hint).setSize(new Dimension(contentSize.width, popupHeight));
    }

    Point point = new Point(visibleRect.x + 5, visibleRect.y + visibleRect.height - newPopupHeight - 20);
    ((AbstractPopup)hint).show(new RelativePoint((Component)frame, point));
  }

  public final Editor createViewer( String code) {
    Intrinsics.checkParameterIsNotNull(code, "createViewer");
    EditorFactory tmp10_7 = EditorFactory.getInstance(); if (tmp10_7 == null) throw new NullPointerException();
    Document tmp24_21 = tmp10_7.createDocument((CharSequence)code); Intrinsics.checkReturnedValueIsNotNull(tmp24_21, "EditorFactory", "createDocument"); Document document = tmp24_21;
    EditorFactory tmp38_35 = EditorFactory.getInstance(); if (tmp38_35 == null) throw new NullPointerException();
    Editor tmp49_46 = tmp38_35.createViewer(document); if (tmp49_46 == null) throw new NullPointerException(); Editor editor = tmp49_46;
    EditorSettings tmp63_58 = editor.getSettings(); Intrinsics.checkReturnedValueIsNotNull(tmp63_58, "Editor", "getSettings"); EditorSettings settings = tmp63_58;
    settings.setAdditionalLinesCount(0);
    settings.setAdditionalColumnsCount(1);
    settings.setRightMarginShown(0);
    settings.setFoldingOutlineShown(0);
    settings.setLineNumbersShown(0);
    settings.setLineMarkerAreaShown(0);
    settings.setIndentGuidesShown(0);
    settings.setVirtualSpace(0);
    settings.setWheelFontChangeEnabled(0);
    settings.setLineCursorWidth(1);
    Editor tmp170_169 = editor; if (tmp170_169 == null) throw new TypeCastException("com.intellij.openapi.editor.Editor cannot be cast to com.intellij.openapi.editor.ex.EditorEx");
    EditorHighlighterFactory tmp191_188 = EditorHighlighterFactory.getInstance(); if (tmp191_188 == null) throw new NullPointerException();
    LanguageFileType tmp205_202 = StdFileTypes.JAVA; Intrinsics.checkFieldIsNotNull(tmp205_202, "StdFileTypes", "JAVA");
    EditorHighlighter tmp221_218 = tmp191_188.createEditorHighlighter(this.project, (FileType)tmp205_202); if (tmp221_218 == null) throw new NullPointerException(); ((EditorEx)tmp170_169).setHighlighter(tmp221_218);
    ((EditorEx)editor).getSelectionModel().addSelectionListener((SelectionListener)new JetObject()
    {

      public void selectionChanged( SelectionEvent event)
      {
        SelectionModel tmp9_4 = this.$editor.getSelectionModel(); Intrinsics.checkReturnedValueIsNotNull(tmp9_4, "Editor", "getSelectionModel"); tmp9_4.removeSelection();
      }
    });
    return editor;
  }


  public final Project getProject()
  {
    return this.project;
  }


  public final Document getDocument()
  {
    return this.document;
  }


  public final String getTargetCode()
  {
    return this.targetCode;
  }


  public final ActionsRecorder getRecorder()
  {
    return this.recorder;
  }

  @JetConstructor
  public RecordingControlPanel( Project project,  Document document,  String targetCode,  ActionsRecorder recorder)
  {
    this.project = project; this.document = document; this.targetCode = targetCode; this.recorder = recorder; this.MAX_WIDTH = 500; this.MAX_HEIGHT = 
      300; this.MIN_HEIGHT = 
      45;

    Disposer.register((Disposable)this.recorder, this);
    setLayout((LayoutManager)new BorderLayout());
    this.myEditor = createViewer(this.targetCode);
    Editor tmp115_112 = this.myEditor; if (tmp115_112 == null) throw new NullPointerException();
    JComponent tmp127_122 = tmp115_112.getComponent(); Intrinsics.checkReturnedValueIsNotNull(tmp127_122, "Editor", "getComponent"); JComponent component = tmp127_122;
    component.setEnabled(0);

    this.myText = new JLabel("Actions: WWW Moving actions: WWW Chars: WWW", SwingConstants.LEFT);
    JPanel topPanel = new JPanel((LayoutManager)new BorderLayout());
    String tmp195_192 = BorderLayout.EAST; Intrinsics.checkFieldIsNotNull(tmp195_192, "BorderLayout", "EAST"); topPanel.add((Component)createControlComponent(), tmp195_192);
    String tmp220_217 = BorderLayout.WEST; Intrinsics.checkFieldIsNotNull(tmp220_217, "BorderLayout", "WEST"); topPanel.add((Component)this.myText, tmp220_217);
    JPanel separator = new JPanel();
    separator.setMinimumSize(new Dimension(20, 5));
    String tmp267_264 = BorderLayout.CENTER; Intrinsics.checkFieldIsNotNull(tmp267_264, "BorderLayout", "CENTER"); topPanel.add((Component)separator, tmp267_264);
    String tmp289_286 = BorderLayout.NORTH; Intrinsics.checkFieldIsNotNull(tmp289_286, "BorderLayout", "NORTH"); add((Component)topPanel, tmp289_286);
    String tmp311_308 = BorderLayout.CENTER; Intrinsics.checkFieldIsNotNull(tmp311_308, "BorderLayout", "CENTER"); add((Component)component, tmp311_308);
    Border tmp332_329 = BorderFactory.createEmptyBorder(0, 3, 3, 3); Intrinsics.checkReturnedValueIsNotNull(tmp332_329, "BorderFactory", "createEmptyBorder"); setBorder(tmp332_329);
    JBPopupFactory tmp349_346 = JBPopupFactory.getInstance(); if (tmp349_346 == null) throw new NullPointerException();
    ComponentPopupBuilder tmp361_358 = tmp349_346.createComponentPopupBuilder(this, this); Intrinsics.checkReturnedValueIsNotNull(tmp361_358, "JBPopupFactory", "createComponentPopupBuilder"); this.myHint = ((JBPopup)KotlinPackage.with(tmp361_358, (ExtensionFunction0)new ExtensionFunctionImpl0()
    {
      public final JBPopup invoke(ComponentPopupBuilder $receiver)
      {
        ComponentPopupBuilder tmp19_14 = $receiver.setRequestFocusCondition(this.this$0.getProject(), (Condition)NotLookupOrSearchCondition.INSTANCE); Intrinsics.checkReturnedValueIsNotNull(tmp19_14, "ComponentPopupBuilder", "setRequestFocusCondition");
        ComponentPopupBuilder tmp39_34 = tmp19_14.setProject(this.this$0.getProject()); Intrinsics.checkReturnedValueIsNotNull(tmp39_34, "ComponentPopupBuilder", "setProject"); tmp39_34;
        ComponentPopupBuilder tmp56_51 = $receiver.setResizable(1); Intrinsics.checkReturnedValueIsNotNull(tmp56_51, "ComponentPopupBuilder", "setResizable");
        ComponentPopupBuilder tmp71_66 = tmp56_51.setMovable(1); Intrinsics.checkReturnedValueIsNotNull(tmp71_66, "ComponentPopupBuilder", "setMovable"); tmp71_66;
        ComponentPopupBuilder tmp88_83 = $receiver.setCancelKeyEnabled(0); Intrinsics.checkReturnedValueIsNotNull(tmp88_83, "ComponentPopupBuilder", "setCancelKeyEnabled"); tmp88_83;
        ComponentPopupBuilder tmp105_100 = $receiver.setRequestFocus(0); Intrinsics.checkReturnedValueIsNotNull(tmp105_100, "ComponentPopupBuilder", "setRequestFocus");
        ComponentPopupBuilder tmp120_115 = tmp105_100.setCancelOnClickOutside(0); Intrinsics.checkReturnedValueIsNotNull(tmp120_115, "ComponentPopupBuilder", "setCancelOnClickOutside"); tmp120_115;
        $receiver.setCancelOnWindowDeactivation(0);
        ComponentPopupBuilder tmp146_141 = $receiver.setTitle("Type Following Code in Editor"); Intrinsics.checkReturnedValueIsNotNull(tmp146_141, "ComponentPopupBuilder", "setTitle"); tmp146_141;
        ComponentPopupBuilder tmp163_158 = $receiver.setModalContext(0); Intrinsics.checkReturnedValueIsNotNull(tmp163_158, "ComponentPopupBuilder", "setModalContext"); tmp163_158;
        ComponentPopupBuilder tmp182_177 = $receiver.addUserData(this.this$0); Intrinsics.checkReturnedValueIsNotNull(tmp182_177, "ComponentPopupBuilder", "addUserData"); tmp182_177;
        JBPopup tmp197_192 = $receiver.createPopup(); Intrinsics.checkReturnedValueIsNotNull(tmp197_192, "ComponentPopupBuilder", "createPopup"); return tmp197_192;
      }
    }));
  }
}