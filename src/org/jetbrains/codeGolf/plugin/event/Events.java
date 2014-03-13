package org.jetbrains.codeGolf.plugin.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.intellij.openapi.components.ServiceManager;

public class Events {
    private final EventBus eventBus = new EventBus();

    private static Events get() {
        return ServiceManager.getService(Events.class);
    }

    public static void post(Object event) {
        get().eventBus.post(event);
    }

    public static void register(Object listener) {
        get(). eventBus.register(listener);
    }
}
