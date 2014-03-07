package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public final class CodeGolfConfigurable implements Configurable {
    private final JPanel mainPanel;
    private final JTextField urlField = new JTextField();
    private final JTextField userNameField = new JTextField();


    public final JPanel getMainPanel() {
        return this.mainPanel;
    }


    public final JTextField getUrlField() {
        return this.urlField;
    }


    public final JTextField getUserNameField() {
        return this.userNameField;
    }

    @Nullable
    public JComponent createComponent() {
        return this.mainPanel;
    }


    public boolean isModified() {
        return !Objects.equal(CodeGolfConfigurableAccessor.getServerUrl(), this.urlField.getText())
                || !Objects.equal(CodeGolfConfigurableAccessor.getUserName(), this.userNameField.getText());
    }


    public void apply() {
        String serverUrl = this.urlField.getText();
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        if (propertiesComponent == null) throw new NullPointerException();
        Preconditions.checkNotNull(CodeGolfSettings.DEFAULT_SERVER_URL, "CodeGolfSettings", "DEFAULT_SERVER_URL");
        if ((!Objects.equal(serverUrl, CodeGolfSettings.DEFAULT_SERVER_URL))) {
            Preconditions.checkNotNull(CodeGolfSettings.SERVER_URL_PROPERTY, "CodeGolfSettings", "SERVER_URL_PROPERTY");
            propertiesComponent.setValue(CodeGolfSettings.SERVER_URL_PROPERTY, serverUrl);
        } else {
            Preconditions.checkNotNull(CodeGolfSettings.SERVER_URL_PROPERTY, "CodeGolfSettings", "SERVER_URL_PROPERTY");
            propertiesComponent.unsetValue(CodeGolfSettings.SERVER_URL_PROPERTY);
        }
        String userNameFieldText = this.userNameField.getText();
        if (userNameFieldText == null) throw new NullPointerException();
        CodeGolfConfigurableAccessor.setUserName(userNameFieldText);
    }


    public void reset() {
        this.urlField.setText(CodeGolfConfigurableAccessor.getServerUrl());
        this.userNameField.setText(CodeGolfConfigurableAccessor.getUserName());
    }


    public void disposeUIResources() {
    }

    @Nls
    public String getDisplayName() {
        return "Code Golf";
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }


    public CodeGolfConfigurable() {
        FormBuilder formBuilder = FormBuilder.createFormBuilder()
                .addLabeledComponent("Server URL:", this.urlField)
                .addLabeledComponent("User name:", this.userNameField);
        JPanel topPanel = formBuilder.getPanel();
        this.mainPanel = new JPanel(new VerticalFlowLayout());
        this.mainPanel.add(topPanel);
    }
}