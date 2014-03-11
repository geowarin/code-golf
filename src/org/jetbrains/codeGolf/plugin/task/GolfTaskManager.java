package org.jetbrains.codeGolf.plugin.task;

import com.google.common.collect.Lists;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.codeGolf.plugin.GolfTask;
import org.jetbrains.codeGolf.plugin.UserScore;
import org.jetbrains.codeGolf.plugin.settings.CodeGolfSettings;

import java.util.ArrayList;
import java.util.List;


public final class GolfTaskManager extends TaskManager {
    private final GolfRestClient golfRestClient;
    private final String serverUrl;

    public GolfTaskManager() {
        golfRestClient = GolfRestClient.getInstance();
        serverUrl = CodeGolfSettings.getServerUrl();
    }

    @Override
    public final List<GolfTask> loadTasks() {
        return new ArrayList<GolfTask>(golfRestClient.loadTasks(serverUrl));
    }

    @Override
    public final List<UserScore> loadScores(@NotNull String userName) {
        if (Strings.isBlank(userName)) {
            return Lists.newArrayList();
        }
        return GolfRestClient.getInstance().loadScores(serverUrl, userName);
    }

    @Override
    public final List<GolfTask> getPredefinedTasks() {
        return PREDIFINED_GOLF_TASKS;
    }

//    public static TaskManager getInstance() {
//        return ServiceManager.getService(GolfTaskManager.class);
//    }
}