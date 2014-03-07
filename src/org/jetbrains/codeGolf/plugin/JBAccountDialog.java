package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
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


    public final JTextField getUsernameField() {
        return this.usernameField;
    }


    public final JPasswordField getPasswordField() {
        return this.passwordField;
    }


    public final JCheckBox getSavePasswordCheckbox() {
        return this.savePasswordCheckbox;
    }


    public final JPanel getMainPanel() {
        return this.mainPanel;
    }


    public final String getUsername() {
        return this.username;
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

    public final Action getLoginAsGuestAction() {
        return this.loginAsGuestAction;
    }


    @NotNull
    protected Action[] createActions() {
        return new Action[]{getOKAction(), this.loginAsGuestAction, getCancelAction()};
    }

    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return this.usernameField;
    }


    protected void doOKAction() {
        String userNameText = this.usernameField.getText();
        Preconditions.checkNotNull(userNameText);

        this.username = userNameText;
        if (Strings.isNotEmpty(this.username)) {

            char[] aThispasswordFieldPassword = this.passwordField.getPassword();
            if (aThispasswordFieldPassword == null) throw new NullPointerException();
            this.password = new String(aThispasswordFieldPassword);
            AuthResult authResult = JBAccountAuthHelper.login(this.username, this.password);
            if (authResult == null) throw new NullPointerException();
            if (!authResult.getIsOk()) {
                Messages.showErrorDialog(this.mainPanel, authResult.getErrorMessage());
                return;
            }

            CodeGolfConfigurableAccessor.setUserName(this.username);
            if (this.savePasswordCheckbox.isSelected()) {
                PasswordSafe passwordSafe = PasswordSafe.getInstance();
                if (passwordSafe == null) throw new NullPointerException();
                try {
                    passwordSafe.storePassword(this.project, LoginWithJBAccountAction.class, CodeGolfConfigurableAccessor.getJB_ACCOUNT_FOR_CODE_GOLF_KEY(), this.password);
                } catch (PasswordSafeException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        doSuperOkAction();
    }

    public final void doSuperOkAction() {
        super.doOKAction();
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return this.mainPanel;
    }


    public final Project getProject() {
        return this.project;
    }

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
        FormBuilder formBuilder = FormBuilder.createFormBuilder().addLabeledComponent("User Name:", this.usernameField);
        this.mainPanel = formBuilder.getPanel();
        this.loginAsGuestAction = new LoginAsGuestAction();
    }


    public final class LoginAsGuestAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            Preconditions.checkNotNull(e, "actionPerformed");
            JBAccountDialog.this.setUsername("");
            JBAccountDialog.this.doSuperOkAction();
        }
    }
}