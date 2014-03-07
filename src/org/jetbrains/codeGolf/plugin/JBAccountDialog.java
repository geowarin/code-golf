package org.jetbrains.codeGolf.plugin;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.util.ui.FormBuilder;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import jet.JetObject;

import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.codeGolf.auth.AuthResult;
import org.jetbrains.codeGolf.auth.JBAccountAuthHelper;


public final class JBAccountDialog extends DialogWrapper
        implements JetObject {
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


    public final void setUsername(String<set-?>) {
        Preconditions.checkNotNull( < set - ?>,"<set-username>");
        this.username =<set - ?>;
    }


    public final String getPassword() {
        return this.password;
    }


    public final void setPassword(String<set-?>) {
        Preconditions.checkNotNull( < set - ?>,"<set-password>");
        this.password =<set - ?>;
    }


    public final Action getLoginAsGuestAction() {
        return this.loginAsGuestAction;
    }


    protected Action[] createActions() {
        return (Action[]) new Action[]{getOKAction(), this.loginAsGuestAction, getCancelAction()};
    }

    @Nullable

    public JComponent getPreferredFocusedComponent() {
        return (JComponent) this.usernameField;
    }


    protected void doOKAction() {
        String tmp8_5 = this.usernameField.getText();
        if (tmp8_5 == null) throw new NullPointerException();
        this.username = tmp8_5;
        if (!KotlinPackage.isEmpty(this.username)) {
            char[] tmp36_33 = this.passwordField.getPassword();
            if (tmp36_33 == null) throw new NullPointerException();
            this.password = KotlinPackage.String(tmp36_33);
            AuthResult tmp60_57 = JBAccountAuthHelper.login(this.username, this.password);
            if (tmp60_57 == null) throw new NullPointerException();
            AuthResult authResult = tmp60_57;
            if (!authResult.getIsOk()) {
                Messages.showErrorDialog((Component) this.mainPanel, authResult.getErrorMessage());
                return;
            }

            PluginPackage.src.CodeGolfConfigurable. - 89488205. setUserName(this.username);
            if (this.savePasswordCheckbox.isSelected()) {
                PasswordSafe tmp110_107 = PasswordSafe.getInstance();
                if (tmp110_107 == null) throw new NullPointerException();
                tmp110_107.storePassword(this.project, LoginWithJBAccountAction.class, PluginPackage.src.CodeGolfConfigurable. - 89488205.getJB_ACCOUNT_FOR_CODE_GOLF_KEY(), this.password);
            }
        }
        doSuperOkAction();
    }

    public final void doSuperOkAction() {
        super.doOKAction();
    }

    @Nullable

    protected JComponent createCenterPanel() {
        return (JComponent) this.mainPanel;
    }


    public final Project getProject() {
        return this.project;
    }

    @JetConstructor
    public JBAccountDialog(Project project) {
        super(project);
        this.project = project;
        this.usernameField =
                new JTextField();
        this.passwordField =
                new JPasswordField();
        this.savePasswordCheckbox =
                new JCheckBox("Remember Password");
        this.username =
                "";
        this.password =
                "";

        setTitle("Login with JetBrains Account");
        HyperlinkLabel hyperlink = new HyperlinkLabel("Create JetBrains Account");
        hyperlink.setHyperlinkTarget("http://account.jetbrains.com/");
        FormBuilder builder;
        FormBuilder tmp88_85 = FormBuilder.createFormBuilder();
        if (tmp88_85 != null)
            tmpTernaryOp = tmp88_85
                    .addLabeledComponent("User Name:", (JComponent) this.usernameField);
    }


    public final class LoginAsGuestAction extends AbstractAction
            implements JetObject {

        public void actionPerformed(ActionEvent e) {
            Preconditions.checkNotNull(e, "actionPerformed");
            JBAccountDialog.this.setUsername("");
            JBAccountDialog.this.doSuperOkAction();
        }

        @JetConstructor
        public LoginAsGuestAction() {
            super();
        }
    }
}