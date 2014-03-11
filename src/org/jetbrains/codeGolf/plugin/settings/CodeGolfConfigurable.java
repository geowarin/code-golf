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
                        .addLabeledComponent("Server URL:", urlField)
                        .addLabeledComponent("User name:", userNameField);
        JPanel topPanel = formBuilder.getPanel();
        mainPanel = new JPanel(new VerticalFlowLayout());
        mainPanel.add(topPanel);
    }

    @Override
    @Nullable
    public JComponent createComponent() {
        return mainPanel;
    }


    @Override
    public boolean isModified() {
        return !Objects.equal(CodeGolfSettings.getServerUrl(), urlField.getText())
                || !Objects.equal(CodeGolfSettings.getUserName(), userNameField.getText());
    }


    @Override
    public void apply() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

        String serverUrl = urlField.getText();
        if (!Objects.equal(serverUrl, CodeGolfSettings.DEFAULT_SERVER_URL)) {
            propertiesComponent.setValue(CodeGolfSettings.SERVER_URL_PROPERTY, serverUrl);
        } else {
            propertiesComponent.unsetValue(CodeGolfSettings.SERVER_URL_PROPERTY);
        }
        CodeGolfSettings.setUserName(userNameField.getText());
    }


    @Override
    public void reset() {
        urlField.setText(CodeGolfSettings.getServerUrl());
        userNameField.setText(CodeGolfSettings.getUserName());
    }


    @Override
    public void disposeUIResources() {
    }

    @Override
    @Nls
    public String getDisplayName() {
        return "Code Golf";
    }

    @Override
    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }
}