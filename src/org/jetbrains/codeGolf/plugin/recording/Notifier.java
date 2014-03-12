package org.jetbrains.codeGolf.plugin.recording;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfResult;
import org.jetbrains.codeGolf.plugin.task.GolfRestClient;

import javax.swing.event.HyperlinkEvent;

public class Notifier {
    private final Project project;
    private final ActionsRecorder.Restarter restarter;

    public Notifier(Project project, ActionsRecorder.Restarter restarter) {
        this.project = project;
        this.restarter = restarter;
    }

    void notifyMouseUsedInEditor() {
        Notifications.Bus.notify(new Notification("mouse on editor", "Don't use mouse on editor!", "mouse actions are worth 1000 actions", NotificationType.WARNING));
    }

    void showCongratulations(GolfResult result) {
        if (result.getResult().equals(result.getBestResult())) {
            notifyBestResult(result);
        } else {
            notifyWinner(result);
        }
    }

    private void notifyWinner(GolfResult result) {
      String url = result.getUrl();
      String message = String.format(
                "Your result is %d. The best result is %d.<br/>" +
			"The details about the task can be found <a href=\"%s\">here</a>.<br/>" +
                        "<a href=\"restart\">Try again</a> if you want to improve your solution.",
		result.getResult(), result.getBestResult(), url);
        Notification notification = new Notification("Registered", "Congratulations", message, NotificationType.INFORMATION, new MyNotificationListener());
        Notifications.Bus.notify(notification, project);
    }

    private void notifyBestResult(GolfResult result) {
        String message = String.format("Your score of %d is the best score registered so far", result.getResult());
        Notification notification = new Notification("Best score", "Congratulations", message, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification, project);
    }

    void showSubmitError(String errorMessage) {
        Notification notification = new Notification("Failed to submit solution", "Gode Golf Error", errorMessage, NotificationType.ERROR);
        Notifications.Bus.notify(notification, project);
    }

    void notifySolutionDiscarded(String reason) {
        Notification notification = new Notification("Code Golf Info", "Solution discarded", reason + "<br/><a href=\"restart\">Try again</a>",
                NotificationType.WARNING, new MyNotificationListener());
        Notifications.Bus.notify(notification, project);
    }

    void notifyUploadError(String errorMessage) {
        Notification notification = new Notification("Code Golf Error", "Cannot upload solution", "Cannot upload solution: " + errorMessage, NotificationType.ERROR);
        Notifications.Bus.notify(notification, project);
    }

    private class MyNotificationListener implements NotificationListener {
        @Override
        public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(event.getEventType()) && "restart".equals(event.getDescription())) {
                notification.expire();
                restarter.restart();
            } else {
                NotificationListener.URL_OPENING_LISTENER.hyperlinkUpdate(notification, event);
            }
        }
    }
}
