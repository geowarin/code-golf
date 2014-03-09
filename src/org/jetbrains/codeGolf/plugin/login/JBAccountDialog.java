package org.jetbrains.codeGolf.plugin.login;

import com.google.common.base.Preconditions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.util.ui.FormBuilder;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.codeGolf.auth.AuthResult;
import org.jetbrains.codeGolf.auth.JBAccountAuthHelper;
import org.jetbrains.codeGolf.plugin.settings.CodeGolfConfigurableAccessor;

import javax.swing.*;
import java.awt.event.ActionEvent;


public final class JBAccountDialog extends DialogWrapper {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JCheckBox savePasswordCheckbox;
    private final JPanel mainPanel;
    private String username;
    private String password;
    private final Action loginAsGuestAction;
    private final Project project;

    public JBAccountDialog(Project project) {
        super(project);
        this.project = project;
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        this.savePasswordCheckbox = new JCheckBox("Remember Password");
        this.username = "";
        this.password = "";

        setTitle("Login With JetBrains Account");
        HyperlinkLabel hyperlink = new HyperlinkLabel("Create JetBrains Account");
        hyperlink.setHyperlinkTarget("http://account.jetbrains.com/");

        FormBuilder formBuilder =
                FormBuilder.createFormBuilder()
                        .addLabeledComponent("User Name:", this.usernameField)
                        .addLabeledComponent("Password:", this.passwordField)
                        .addLabeledComponent("Remember Password:", this.savePasswordCheckbox)
                        .addComponent(hyperlink);
        this.mainPanel = formBuilder.getPanel();
        this.loginAsGuestAction = new LoginAsGuestAction();
        init();
    }

    @NotNull
    protected Action[] createActions() {
        return new Action[]{getOKAction(), this.loginAsGuestAction, getCancelAction()};
    }

    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return this.usernameField;
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return this.mainPanel;
    }

    protected void doOKAction() {
        String userNameText = this.usernameField.getText();
        Preconditions.checkNotNull(userNameText);

        this.username = userNameText;
        if (Strings.isNotEmpty(this.username)) {

            this.password = new String(this.passwordField.getPassword());
            AuthResult authResult = JBAccountAuthHelper.login(this.username, this.password);
            if (!authResult.getIsOk()) {
                Messages.showErrorDialog(this.mainPanel, authResult.getErrorMessage());
                return;
            }

            CodeGolfConfigurableAccessor.setUserName(this.username);
            if (this.savePasswordCheckbox.isSelected()) {
                CodeGolfConfigurableAccessor.savePassword(this.project, this.password);
            }
        }
        doSuperOkAction();
    }

    public final void doSuperOkAction() {
        super.doOKAction();
    }

    public final class LoginAsGuestAction extends AbstractAction {
        public LoginAsGuestAction() {
            putValue(NAME, "Login as Guest");
            putValue(SHORT_DESCRIPTION, "Log in anonymously");
        }

        public void actionPerformed(ActionEvent e) {
            JBAccountDialog.this.setUsername("");
            JBAccountDialog.this.doSuperOkAction();
        }
    }

    public final String getUsername() {
        return this.username;
    }

    public final Project getProject() {
        return this.project;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public final String getPassword() {
        return this.password;
    }
}