package org.jetbrains.codeGolf.plugin.recording;

import com.google.common.base.Joiner;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.EditorMouseAdapter;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfSolution;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.IdeaUtils;
import org.jetbrains.codeGolf.plugin.controlpanel.RecordingControlPanel;
import org.jetbrains.codeGolf.plugin.login.Credentials;

public final class ActionsRecorder implements Disposable {
    private final Score score = new Score();
    private final EditorMouseAdapter editorMouseListener = new PenaliseMouseOnEditorListener();
    private final RecordingControlPanel controlPanel;
    private final GolfTask golfTask;
    private final Project project;
    private final Document document;
    private final Credentials credentials;
    private final Restarter restarter;
    private final Notifier notifier;

    private boolean disposed;
    private boolean recording;
    private static final Logger LOG = Logger.getInstance("#org.jetbrains.codeGolf");
    private final Editor editor;

    public ActionsRecorder(@NotNull GolfTask golfTask, @NotNull Project project, @NotNull Document document, Credentials credentials, Restarter restarter) {
        this.golfTask = golfTask;
        this.project = project;
        this.document = document;
        this.credentials = credentials;
        this.restarter = restarter;
        editor = IdeaUtils.getEditor(project, document);
        controlPanel = new RecordingControlPanel(project, document, golfTask.getTargetCode(), this);
        notifier = new Notifier(project, restarter);
    }

    public final void startRecording() {
        recording = true;
        LOG.info("recording started");
        controlPanel.showHint();

        editor.addEditorMouseListener(editorMouseListener);
        document.addDocumentListener(new SolutionFoundDocumentListener(), this);
        ActionManager.getInstance().addAnActionListener(new RecordingActionListener(score, controlPanel, this), this);
    }

    public final void stopRecording() {
        LOG.info("Recording stopped");
        recording = false;
        editor.removeEditorMouseListener(editorMouseListener);
        Disposer.dispose(this);
    }

    public void discardSolution(String reason) {
        stopRecording();
        notifier.notifySolutionDiscarded(reason);
    }

    public final Restarter getRestarter() {
        return restarter;
    }

    public interface Restarter {
        void restart();
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isTaskSolved() {
        return !disposed && new TaskSolutionChecker(golfTask).checkSolution(document.getText());
    }

    private class SolutionFoundDocumentListener extends DocumentAdapter {
        @Override
        public void documentChanged(DocumentEvent event) {
            if (isTaskSolved()) {
                GolfSolution golfSolution = createGolfSolution(golfTask.getTaskId(), score);
                new SolutionSender(project, credentials, ActionsRecorder.this).sendSolutionWhenPossible(golfSolution);
            }
        }

        private GolfSolution createGolfSolution(String taskId, Score score) {
            return new GolfSolution(taskId, credentials.getUserName(), score.getMovingActionsCounter(), score.getTypingCounter(), score.getActionsCounter(), Joiner.on('|').join(score.getUsedActions()));
        }
    }

    @Override
    public void dispose() {
        disposed = true;
        recording = false;
    }

    private class PenaliseMouseOnEditorListener extends EditorMouseAdapter {
        @Override
        public void mouseClicked(EditorMouseEvent e) {
            notifier.notifyMouseUsedInEditor();
            score.increaseMovingActions(1000);
            controlPanel.notifyUser(score);
        }
    }

}