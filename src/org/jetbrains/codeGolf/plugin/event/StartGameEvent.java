package org.jetbrains.codeGolf.plugin.event;

import com.intellij.openapi.project.Project;
import org.jetbrains.codeGolf.plugin.login.Credentials;

public class StartGameEvent {
    public StartGameEvent(Project project, Credentials credentials) {
    }
}
