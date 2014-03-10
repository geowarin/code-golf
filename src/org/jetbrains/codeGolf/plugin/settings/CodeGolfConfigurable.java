package org.jetbrains.codeGolf.plugin.settings;

import com.google.common.base.Objects;
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

    public CodeGolfConfigurable() {
        FormBuilder formBuilder =
                FormBuilder.createFormBuilder()
                        .addLabeledComponent("Server URL:", this.urlField)
                        .addLabeledComponent("User name:", this.userNameField);
        JPanel topPanel = formBuilder.getPanel();
        this.mainPanel = new JPanel(new VerticalFlowLayout());
        this.mainPanel.add(topPanel);
    }

    @Nullable
    public JComponent createComponent() {
        return this.mainPanel;
    }


    public boolean isModified() {
        return !Objects.equal(CodeGolfSettings.getServerUrl(), this.urlField.getText())
                || !Objects.equal(CodeGolfSettings.getUserName(), this.userNameField.getText());
    }


    public void apply() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

        String serverUrl = this.urlField.getText();
        if (!Objects.equal(serverUrl, CodeGolfSettings.DEFAULT_SERVER_URL)) {
            propertiesComponent.setValue(CodeGolfSettings.SERVER_URL_PROPERTY, serverUrl);
        } else {
            propertiesComponent.unsetValue(CodeGolfSettings.SERVER_URL_PROPERTY);
        }
        CodeGolfSettings.setUserName(this.userNameField.getText());
    }


    public void reset() {
        this.urlField.setText(CodeGolfSettings.getServerUrl());
        this.userNameField.setText(CodeGolfSettings.getUserName());
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
}