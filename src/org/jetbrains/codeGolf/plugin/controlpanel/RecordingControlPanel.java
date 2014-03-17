package org.jetbrains.codeGolf.plugin.controlpanel;

import com.google.common.base.Preconditions;
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
import org.jetbrains.codeGolf.plugin.recording.ActionsRecorder;
import org.jetbrains.codeGolf.plugin.recording.Score;

import javax.swing.*;
import java.awt.*;


public final class RecordingControlPanel extends JPanel implements Disposable {
    private Editor myEditor;
    private final JLabel scoreText;
    public final JBPopup myHint;
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
        myEditor = createViewer(this.targetCode);

        JComponent component = myEditor.getComponent();
        component.setEnabled(false);

        scoreText = new JLabel("Actions: WWW Moving actions: WWW Chars: WWW", SwingConstants.LEFT);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createControlComponent(), BorderLayout.EAST);
        topPanel.add(scoreText, BorderLayout.WEST);

        JPanel separator = new JPanel();
        separator.setMinimumSize(new Dimension(20, 5));
        topPanel.add(separator, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(component, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));

        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(this, this);
        myHint = createHint(componentPopupBuilder);
        notifyUser(new Score());
    }

    @Override
    public void dispose() {
        myHint.dispose();
        if (myEditor != null) {
            EditorFactory.getInstance().releaseEditor(myEditor);
            myEditor = null;
        }
    }

    public final JComponent createControlComponent() {
        DefaultActionGroup group = new DefaultActionGroup(
                new ShowDiffWithExpectedAction(targetCode, document),
                new NavigateToEditorAction(document),
                Separator.getInstance(),
                new TryAgainAction(recorder),
                new StopSolvingAction(recorder)
        );
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("CodeGolfToolbar", group, true);
        return actionToolbar.getComponent();
    }

    public final void notifyUser(Score score) {
        String message = String.format(
                "<html>Actions: <b>%s</b>.&nbsp; Moving actions: <b>%s</b>.&nbsp; Chars: <b>%s</b>.</html>",
                score.getActionsCounter(), score.getMovingActionsCounter(), score.getTypingCounter()
        );
        notifyUser(message);
    }

    public final void notifyUser(String message) {
        scoreText.setText(message);
        scoreText.revalidate();
        scoreText.repaint();
    }

    public final void showHint() {
        WindowManager windowManager = WindowManager.getInstance();
        IdeFrame ideFrame = windowManager.getIdeFrame(project);
        JComponent frame = ideFrame.getComponent();

        Rectangle visibleRect = frame.getVisibleRect();
        Dimension contentSize = getPreferredSize();

        Dimension hintSize = ((AbstractPopup) myHint).getHeaderPreferredSize();
        int popupHeight = contentSize.height + hintSize.height;
        int newPopupHeight = Math.max(Math.min(popupHeight, visibleRect.height / 2), 150);
        if (newPopupHeight != popupHeight) {
            popupHeight = newPopupHeight;
            myHint.setSize(new Dimension(contentSize.width, popupHeight));
        }

        Point point = new Point(visibleRect.x + 5, visibleRect.y + visibleRect.height - newPopupHeight - 20);
        myHint.show(new RelativePoint(frame, point));
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

        EditorHighlighter editorHighlighter = editorHighlighterFactory.createEditorHighlighter(project, StdFileTypes.JAVA);
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
                .setRequestFocusCondition(project, NotLookupOrSearchCondition.INSTANCE)
                .setProject(project)
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