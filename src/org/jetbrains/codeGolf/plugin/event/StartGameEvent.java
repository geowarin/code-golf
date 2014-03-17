package org.jetbrains.codeGolf.plugin.event;

import com.intellij.openapi.project.Project;

public class StartGameEvent {
    private Project project;

    public StartGameEvent(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
