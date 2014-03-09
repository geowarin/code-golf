package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class GolfTaskManager {
    private final List<GolfTask> tasks = Arrays.asList(new GolfTask(null, null, "Hello World", "public class HelloWorld {}", 0, "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, world!\");\n    }\n}\n        ", null));

    public final List<GolfTask> getTasks() {
        return this.tasks;
    }

    public final List<GolfTask> loadTasks(@NotNull String serverUrl) {
        return new ArrayList<GolfTask>(RestClientUtil.loadTasks(serverUrl));
    }

    public final List<UserScore> loadScores(@NotNull String serverUrl, @NotNull String username) {
        return new ArrayList<UserScore>(RestClientUtil.loadScores(serverUrl, username));
    }

    public final List<GolfTask> getPredefinedTasks() {
        return this.tasks;
    }

    public final void addTask(@NotNull GolfTask task) {
        this.tasks.add(task);
    }

    public static GolfTaskManager getInstance() {
        return ServiceManager.getService(GolfTaskManager.class);
    }
}