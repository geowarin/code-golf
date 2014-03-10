package org.jetbrains.codeGolf.plugin.task;

import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.UserScore;

import java.util.Arrays;
import java.util.List;

/**
 * Date: 10/03/2014
 * Time: 22:57
 *
 * @author Geoffroy Warin (http://geowarin.github.io)
 */
public abstract class TaskManager {
    List<GolfTask> PREDIFINED_GOLF_TASKS = Arrays.asList(new GolfTask(null, null, "Hello World", "public class HelloWorld {}", 0, "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, world!\");\n    }\n}\n        ", null));

    public abstract List<GolfTask> loadTasks();

    public abstract List<UserScore> loadScores(@NotNull String username);

    public abstract List<GolfTask> getPredefinedTasks();

    public static TaskManager getInstance() {
        return ServiceManager.getService(TaskManager.class);
    }
}
