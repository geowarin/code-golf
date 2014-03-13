package org.jetbrains.codeGolf.plugin.recording;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfResult;
import org.jetbrains.codeGolf.plugin.GolfSolution;
import org.jetbrains.codeGolf.plugin.login.Credentials;
import org.jetbrains.codeGolf.plugin.task.GolfRestClient;

class SolutionSender {
    private final ActionsRecorder recorder;
    private final Score score;
    private final Credentials credentials;
    private final Project project;
    private final Notifier notifier;

    SolutionSender(Project project, Score score, Credentials credentials, ActionsRecorder recorder) {
        this.project = project;
        this.score = score;
        this.credentials = credentials;
        this.recorder = recorder;
        notifier = new Notifier(project, recorder.getRestarter());
    }

    public void sendSolutionWhenPossible(final GolfSolution solution) {
        Application application = ApplicationManager.getApplication();
        application.invokeLater(new Runnable() {
            public final void run() {
                if (recorder.isTaskSolved()) {
                    try {
                        trySendSolutionToServer(solution);
                    } finally { // Dispose must be done in dispatch thread
                        recorder.stopRecording();
                    }
                }
            }
        });
    }

    private void trySendSolutionToServer(final GolfSolution solution) {
        new Task.Backgroundable(project, "sending data") {
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    sendSolution(solution);
                } catch (Exception exception) {
                    notifier.notifyUploadError(exception.getMessage());
                }
            }
        }.queue();
    }

    private void sendSolution(GolfSolution solution) {
        GolfResult golfResult = GolfRestClient.getInstance().sendSolution(solution, credentials.getToken());
        if (golfResult.getErrorMessage() != null) {
            notifier.showSubmitError(golfResult.getErrorMessage());
        } else {
            notifier.showCongratulations(golfResult);
        }
    }
}
