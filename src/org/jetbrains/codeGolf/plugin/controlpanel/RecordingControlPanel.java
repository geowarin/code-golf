package org.jetbrains.codeGolf.plugin.controlpanel;

import com.google.common.base.Preconditions;
import com.intellij.ide.IdePopupManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.ui.popup.NotLookupOrSearchCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.ActionsRecorder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public final class RecordingControlPanel extends JPanel implements Disposable {
    private Editor myEditor;
    private final JLabel myText;
    private final JBPopup myHint;
    private final int MAX_WIDTH = 500;
    private final int MAX_HEIGHT = 300;
    private final int MIN_HEIGHT = 45;
    private final Project project;
    private final Document document;
    private final String targetCode;
    private final ActionsRecorder recorder;

    public RecordingControlPanel(Project project, Document document, String targetCode, ActionsRecorder recorder) {
        this.project = project;
        this.document = document;
        this.targetCode = targetCode;
        this.recorder = recorder;

        Disposer.register(this.recorder, this);
        setLayout(new BorderLayout());
        this.myEditor = createViewer(this.targetCode);
        if (this.myEditor == null) throw new NullPointerException();

        JComponent component = this.myEditor.getComponent();
        Preconditions.checkNotNull(this.myEditor, "Editor", "getComponent");
        component.setEnabled(false);

        this.myText = new JLabel("Actions: WWW Moving actions: WWW Chars: WWW", SwingConstants.LEFT);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createControlComponent(), BorderLayout.EAST);
        topPanel.add(this.myText, BorderLayout.WEST);

        JPanel separator = new JPanel();
        separator.setMinimumSize(new Dimension(20, 5));
        topPanel.add(separator, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(component, BorderLayout.CENTER);

        Border border = BorderFactory.createEmptyBorder(0, 3, 3, 3);
        setBorder(border);

        JBPopupFactory popupFactory = JBPopupFactory.getInstance();

        ComponentPopupBuilder componentPopupBuilder = popupFactory.createComponentPopupBuilder(this, this);
        this.myHint = createHint(componentPopupBuilder);
        notifyUser(0, 0, 0);
    }

    public void dispose() {
        if (this.myEditor == null) {
            EditorFactory.getInstance().releaseEditor(this.myEditor);
            this.myEditor = null;
        }
//        myHint.setUiVisible(false);
        myHint.cancel();
//        Disposer.dispose(myHint);
    }

    public final JComponent createControlComponent() {
        DefaultActionGroup group = new DefaultActionGroup(
                new ShowDiffWithExpectedAction(this.targetCode, this.document),
                new NavigateToEditorAction(this.document),
                Separator.getInstance(),
                new TryAgainAction(this.recorder),
                new StopSolvingAction(this.recorder));
        ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar actionToolbar = actionManager.createActionToolbar("CodeGolfToolbar", group, true);
        return actionToolbar.getComponent();
    }

    public final void notifyUser(int actionsCounter, int movingActionsCounter, int typingCounter) {
        notifyUser("<html>Actions: <b>" + actionsCounter + "</b>.&nbsp; Moving actions: <b>" + movingActionsCounter + "</b>.&nbsp; Chars: <b>" + typingCounter + "</b>.</html>");
    }

    public final void notifyUser(String message) {
        Preconditions.checkNotNull(message, "notifyUser");
        this.myText.setText(message);
        this.myText.revalidate();
        this.myText.repaint();
    }

    public final void showHint() {
        WindowManager windowManager = WindowManager.getInstance();
        IdeFrame ideFrame = windowManager.getIdeFrame(this.project);
        JComponent frame = ideFrame.getComponent();

        Rectangle visibleRect = frame.getVisibleRect();
        Dimension contentSize = getPreferredSize();

        Dimension hintSize = ((AbstractPopup) this.myHint).getHeaderPreferredSize();
        int popupHeight = contentSize.height + hintSize.height;
        int newPopupHeight = Math.max(Math.min(popupHeight, visibleRect.height / 2), 150);
        if (newPopupHeight != popupHeight) {
            popupHeight = newPopupHeight;
            this.myHint.setSize(new Dimension(contentSize.width, popupHeight));
        }

        Point point = new Point(visibleRect.x + 5, visibleRect.y + visibleRect.height - newPopupHeight - 20);
        this.myHint.show(new RelativePoint(frame, point));
    }

    public final Editor createViewer(@NotNull String code) {

        EditorFactory editorFactory = EditorFactory.getInstance();
        Document document = editorFactory.createDocument(code);
        final Editor editor = editorFactory.createViewer(document);

        EditorSettings settings = editor.getSettings();
        Preconditions.checkNotNull(settings, "Editor", "getSettings");
        settings.setAdditionalLinesCount(0);
        settings.setAdditionalColumnsCount(1);
        settings.setRightMarginShown(false);
        settings.setFoldingOutlineShown(false);
        settings.setLineNumbersShown(false);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setVirtualSpace(false);
        settings.setWheelFontChangeEnabled(false);
        settings.setLineCursorWidth(1);

        EditorHighlighterFactory editorHighlighterFactory = EditorHighlighterFactory.getInstance();

        EditorHighlighter editorHighlighter = editorHighlighterFactory.createEditorHighlighter(this.project, StdFileTypes.JAVA);
        ((EditorEx) editor).setHighlighter(editorHighlighter);
        editor.getSelectionModel().addSelectionListener(new SelectionListener() {

            public void selectionChanged(SelectionEvent event) {

                SelectionModel selectionModel = editor.getSelectionModel();
                selectionModel.removeSelection();
            }
        });
        return editor;
    }

    private JBPopup createHint(ComponentPopupBuilder componentPopupBuilder) {
        return componentPopupBuilder
                .setRequestFocusCondition(this.project, NotLookupOrSearchCondition.INSTANCE)
                .setProject(this.project)
                .setResizable(true)
                .setMovable(true)
                .setCancelKeyEnabled(false)
                .setRequestFocus(false)
                .setCancelOnClickOutside(false)
                .setCancelOnWindowDeactivation(false)
                .setTitle("Type Following Code in Editor")
                .setModalContext(false)
                .addUserData(this).createPopup();
    }
}