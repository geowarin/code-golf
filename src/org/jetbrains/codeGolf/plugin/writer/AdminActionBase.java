package org.jetbrains.codeGolf.plugin.writer;

import com.google.common.base.Preconditions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.auth.AuthPackage;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.login.Credentials;
import org.jetbrains.codeGolf.plugin.login.LoginService;
import org.jetbrains.codeGolf.plugin.settings.CodeGolfSettings;


public abstract class AdminActionBase extends AnAction {

    public void update(AnActionEvent e) {
        if (e == null) throw new NullPointerException();
        Presentation presentation = e.getPresentation();
        Preconditions.checkNotNull(presentation, "AnActionEvent", "getPresentation");
        Preconditions.checkNotNull(e.getProject(), "AnActionEvent", "getPresentation");
        presentation.setVisible(AuthPackage.hasAdminAccess(CodeGolfSettings.getUserName()));
    }


    public void actionPerformed(AnActionEvent e) {
        if (e == null) throw new NullPointerException();
        Project project = e.getProject();
        Preconditions.checkNotNull(project);

        Credentials credentials = LoginService.getInstance().showDialogAndLogin(project);
        if (credentials != null)
            return;

        if (!AuthPackage.hasAdminAccess(credentials.getUserName())) {
            Messages.showErrorDialog(project, "This action can be performed by admins only", "Code Golf Error");
            return;
        }
        doAdminAction(project, e, credentials.getUserName(), credentials.getToken());
    }

    protected abstract void doAdminAction(Project paramProject, AnActionEvent paramAnActionEvent, String paramString1, String paramString2);

    public static void sendTaskToServer(@NotNull Project project, @NotNull GolfTask task, @NotNull String password) {
        new Task.Backgroundable(project, password) { // ERROR //

            public void run(ProgressIndicator indicator) { // Byte code:
                //   0: aload_1
                //   1: ldc 33
                //   3: invokestatic 39	jet/runtime/Intrinsics:checkParameterIsNotNull	(Ljava/lang/Object;Ljava/lang/String;)V
                //   6: nop
                //   7: aload_0
                //   8: getfield 43	org/jetbrains/codeGolf/plugin/writer/AdminActionBase$object$sendTaskToServer$1:$task	Lorg/jetbrains/codeGolf/plugin/GolfTask;
                //   11: aload_0
                //   12: getfield 47	org/jetbrains/codeGolf/plugin/writer/AdminActionBase$object$sendTaskToServer$1:$password	Ljava/lang/String;
                //   15: invokestatic 53	org/jetbrains/codeGolf/plugin/rest/RestClientUtil:sendNewTask	(Lorg/jetbrains/codeGolf/plugin/GolfTask;Ljava/lang/String;)Ljava/lang/String;
                //   18: dup
                //   19: ifnonnull +6 -> 25
                //   22: invokestatic 57	jet/runtime/Intrinsics:throwNpe	()V
                //   25: astore_2
                //   26: aload_2
                //   27: ldc 59
                //   29: invokestatic 65	kotlin/KotlinPackage:startsWith	(Ljava/lang/String;Ljava/lang/String;)Z
                //   32: ifeq +34 -> 66
                //   35: aload_2
                //   36: astore_3
                //   37: new 67	com/intellij/notification/Notification
                //   40: dup
                //   41: ldc 69
                //   43: ldc 71
                //   45: aload_3
                //   46: getstatic 77	com/intellij/notification/NotificationType:ERROR	Lcom/intellij/notification/NotificationType;
                //   49: invokespecial 81	com/intellij/notification/Notification:<init>	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/intellij/notification/NotificationType;)V
                //   52: astore 4
                //   54: aload 4
                //   56: aload_0
                //   57: getfield 85	org/jetbrains/codeGolf/plugin/writer/AdminActionBase$object$sendTaskToServer$1:$project	Lcom/intellij/openapi/project/Project;
                //   60: invokestatic 91	com/intellij/notification/Notifications$Bus:notify	(Lcom/intellij/notification/Notification;Lcom/intellij/openapi/project/Project;)V
                //   63: goto +93 -> 156
                //   66: new 96	java/lang/StringBuilder
                //   69: dup
                //   70: invokespecial 98	java/lang/StringBuilder:<init>	()V
                //   73: ldc 100
                //   75: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   78: new 96	java/lang/StringBuilder
                //   81: dup
                //   82: invokespecial 98	java/lang/StringBuilder:<init>	()V
                //   85: ldc 106
                //   87: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   90: ldc 108
                //   92: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   95: aload_2
                //   96: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   99: ldc 108
                //   101: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   104: ldc 110
                //   106: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   109: invokevirtual 114	java/lang/StringBuilder:toString	()Ljava/lang/String;
                //   112: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   115: invokevirtual 114	java/lang/StringBuilder:toString	()Ljava/lang/String;
                //   118: astore_3
                //   119: new 67	com/intellij/notification/Notification
                //   122: dup
                //   123: ldc 69
                //   125: ldc 116
                //   127: aload_3
                //   128: getstatic 119	com/intellij/notification/NotificationType:INFORMATION	Lcom/intellij/notification/NotificationType;
                //   131: getstatic 125	com/intellij/notification/NotificationListener:URL_OPENING_LISTENER	Lcom/intellij/notification/NotificationListener;
                //   134: dup
                //   135: ldc 127
                //   137: ldc 128
                //   139: invokestatic 132	jet/runtime/Intrinsics:checkFieldIsNotNull	(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
                //   142: invokespecial 135	com/intellij/notification/Notification:<init>	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/intellij/notification/NotificationType;Lcom/intellij/notification/NotificationListener;)V
                //   145: astore 4
                //   147: aload 4
                //   149: aload_0
                //   150: getfield 85	org/jetbrains/codeGolf/plugin/writer/AdminActionBase$object$sendTaskToServer$1:$project	Lcom/intellij/openapi/project/Project;
                //   153: invokestatic 91	com/intellij/notification/Notifications$Bus:notify	(Lcom/intellij/notification/Notification;Lcom/intellij/openapi/project/Project;)V
                //   156: goto +52 -> 208
                //   159: astore_2
                //   160: new 67	com/intellij/notification/Notification
                //   163: dup
                //   164: ldc 138
                //   166: ldc 140
                //   168: new 96	java/lang/StringBuilder
                //   171: dup
                //   172: invokespecial 98	java/lang/StringBuilder:<init>	()V
                //   175: ldc 142
                //   177: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   180: aload_2
                //   181: dup
                //   182: ifnull +9 -> 191
                //   185: invokevirtual 147	java/lang/Throwable:getMessage	()Ljava/lang/String;
                //   188: goto +5 -> 193
                //   191: pop
                //   192: aconst_null
                //   193: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   196: invokevirtual 114	java/lang/StringBuilder:toString	()Ljava/lang/String;
                //   199: getstatic 77	com/intellij/notification/NotificationType:ERROR	Lcom/intellij/notification/NotificationType;
                //   202: invokespecial 81	com/intellij/notification/Notification:<init>	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/intellij/notification/NotificationType;)V
                //   205: invokestatic 150	com/intellij/notification/Notifications$Bus:notify	(Lcom/intellij/notification/Notification;)V
                //   208: return
                //
                // Exception table:
                //   from	to	target	type
                //   6	156	159	java/lang/Throwable }  } .queue();
            }
        };
    }
}