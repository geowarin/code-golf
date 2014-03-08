package org.jetbrains.codeGolf.plugin.writer;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.FormBuilder;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfTask;

public class BaloonManager {
    public static void ShowBalloonForTransformTask(final String oldCode, final int initialOffset, final PsiFile psiFile, final Document document, final Project project, final String username, final String password) {
        JComponent component = WindowManager.getInstance().getIdeFrame(null).getStatusBar().getComponent();
        JButton button = new JButton("Submit new transform task");
        final Ref<Balloon> ref = new Ref();
        button.addMouseListener(new MouseAdapter() {

            public void mouseClicked(@NotNull MouseEvent mouseEvent) {
                if (ref.isNull() || ref.get().isDisposed()) return;
                ref.get().dispose();
                ref.set(null);
                String defaultTaskName = psiFile.getVirtualFile().getNameWithoutExtension();
                GolfTask task = BaloonManager.createTask(defaultTaskName, oldCode, initialOffset, document.getText(), project, username);
                if (task != null)
                    AdminActionBase.sendTaskToServer(project, task, password);
            }
        });
        BalloonBuilder builder = JBPopupFactory.getInstance().createBalloonBuilder(button);
        builder.setHideOnClickOutside(false);
        builder.setHideOnKeyOutside(false);
        builder.setCloseButtonEnabled(true);
        builder.setHideOnAction(false);
        Balloon balloon = builder.createBalloon();
        ref.set(balloon);
        balloon.showInCenterOf(component);
    }

    public static GolfTask createTask(String defaultTaskName, String oldCode, int initialOffset, String targetCode, Project project, String username) {
        JTextField taskNameField = new JTextField(defaultTaskName);
        taskNameField.setPreferredSize(new Dimension(120, taskNameField.getPreferredSize().height));
        JTextField hintField = new JTextField();
        JPanel panel = FormBuilder.createFormBuilder().addLabeledComponent("Task Name:", taskNameField).addLabeledComponent("Hint (optional):", hintField).getPanel();

        DialogBuilder builder = new DialogBuilder(project);
        builder.setTitle("Create New Task");
        builder.setCenterPanel(panel);
        builder.setPreferredFocusComponent(taskNameField);
        if (builder.show() == 0) {
            String taskName = taskNameField.getText();
            return new GolfTask(username, "0", taskName, oldCode, initialOffset, targetCode, hintField.getText());
        }
        return null;
    }
}