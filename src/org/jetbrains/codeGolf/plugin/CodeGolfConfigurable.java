package org.jetbrains.codeGolf.plugin;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.util.ui.FormBuilder;
import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


public final class CodeGolfConfigurable
        implements JetObject, Configurable {
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
        return (JComponent) this.mainPanel;
    }


    public boolean isModified() {
        return !(Intrinsics.areEqual(PluginPackage.src.CodeGolfConfigurable. - 89488205.getServerUrl(), this.urlField.getText()) ^ true) ? Intrinsics.areEqual(PluginPackage.src.CodeGolfConfigurable. - 89488205.getUserName(), this.userNameField.getText()) ^ true : true;
    }


    public void apply() {
        String serverUrl = this.urlField.getText();
        PropertiesComponent tmp11_8 = PropertiesComponent.getInstance();
        if (tmp11_8 == null) throw new NullPointerException();
        PropertiesComponent propertiesComponent = tmp11_8;
        String tmp23_20 = CodeGolfSettings.DEFAULT_SERVER_URL;
        Intrinsics.checkFieldIsNotNull(tmp23_20, "CodeGolfSettings", "DEFAULT_SERVER_URL");
        if ((!Intrinsics.areEqual(serverUrl, tmp23_20))) {
            String tmp43_40 = CodeGolfSettings.SERVER_URL_PROPERTY;
            Intrinsics.checkFieldIsNotNull(tmp43_40, "CodeGolfSettings", "SERVER_URL_PROPERTY");
            propertiesComponent.setValue(tmp43_40, serverUrl);
        } else {
            String tmp62_59 = CodeGolfSettings.SERVER_URL_PROPERTY;
            Intrinsics.checkFieldIsNotNull(tmp62_59, "CodeGolfSettings", "SERVER_URL_PROPERTY");
            propertiesComponent.unsetValue(tmp62_59);
        }
        String tmp80_77 = this.userNameField.getText();
        if (tmp80_77 == null) throw new NullPointerException();
        PluginPackage.src.CodeGolfConfigurable. - 89488205. setUserName(tmp80_77);
    }


    public void reset() {
        this.urlField.setText(PluginPackage.src.CodeGolfConfigurable. - 89488205.getServerUrl());
        this.userNameField.setText(PluginPackage.src.CodeGolfConfigurable. - 89488205.getUserName());
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
        return (String) null;
    }


    @JetConstructor
    public CodeGolfConfigurable() {
        FormBuilder tmp29_26 = FormBuilder.createFormBuilder();
        if (tmp29_26 == null) throw new NullPointerException();
        FormBuilder tmp48_45 = tmp29_26
                .addLabeledComponent("Server URL:", (JComponent) this.urlField);
        if (tmp48_45 == null) throw new NullPointerException();
        FormBuilder tmp67_64 = tmp48_45
                .addLabeledComponent("User name:", (JComponent) this.userNameField);
        if (tmp67_64 == null) throw new NullPointerException();
        JPanel tmp77_74 = tmp67_64.getPanel();
        if (tmp77_74 == null) throw new NullPointerException();
        JPanel topPanel = tmp77_74;

        this.mainPanel = new JPanel((LayoutManager) new VerticalFlowLayout());
        this.mainPanel.add((Component) topPanel);
    }
}