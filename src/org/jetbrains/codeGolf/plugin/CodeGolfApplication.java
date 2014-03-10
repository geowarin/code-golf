package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.impl.ComponentManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.login.LoginService;
import org.jetbrains.codeGolf.plugin.login.LoginWithJBAccount;
import org.picocontainer.MutablePicoContainer;

/**
 * Date: 10/03/2014
 * Time: 21:24
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public class CodeGolfApplication implements ApplicationComponent {

    private final ComponentManagerImpl componentManager;

    public CodeGolfApplication(@NotNull ComponentManagerImpl componentManager) {
        this.componentManager = componentManager;
    }

    @Override
    public void initComponent() {
        MutablePicoContainer picoContainer = componentManager.getPicoContainer();
        picoContainer.registerComponentInstance(LoginService.class.getName(), new LoginWithJBAccount());
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "CodeGolfApplication";
    }
}
