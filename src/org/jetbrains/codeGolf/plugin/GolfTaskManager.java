package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.components.ServiceManager;

import java.util.ArrayList;
import java.util.List;

import jet.Function1;
import jet.FunctionImpl1;
import jet.JetObject;
import jet.Unit;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetClassObject;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;
import org.jetbrains.codeGolf.plugin.rest.RestClientUtil;


public final class GolfTaskManager
        implements JetObject {
    private final List tasks = (List) KotlinPackage.arrayListOf(new GolfTask[]{new GolfTask(null, null, "Hello World", "public class HelloWorld {}", 0, "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, world!\");\n    }\n}\n        ", null, 83)});
    public static final object object$ = new object();


    public final List<GolfTask> getTasks() {
        return this.tasks;
    }


    public final List<GolfTask> loadTasks(String serverUrl) {
        Intrinsics.checkParameterIsNotNull(serverUrl, "loadTasks");
        ArrayList result = new ArrayList();
        List tmp18_15 = RestClientUtil.loadTasks(serverUrl);
        if (tmp18_15 != null) KotlinPackage.forEach((Iterable) tmp18_15, (Function1) new FunctionImpl1() {
            public final void invoke(GolfTask it) {
                Intrinsics.checkParameterIsNotNull(it, "<anonymous>");
                this.$result.add(it);
            }
        });
    }


    public final List<UserScore> loadScores(String serverUrl, String username) {
        Intrinsics.checkParameterIsNotNull(serverUrl, "loadScores");
        Intrinsics.checkParameterIsNotNull(username, "loadScores");
        ArrayList result = new ArrayList();
        List tmp25_22 = RestClientUtil.loadScores(serverUrl, username);
        if (tmp25_22 != null) KotlinPackage.forEach((Iterable) tmp25_22, (Function1) new FunctionImpl1() {
            public final void invoke(UserScore it) {
                Intrinsics.checkParameterIsNotNull(it, "<anonymous>");
                this.$result.add(it);
            }
        });
    }


    public final List<GolfTask> getPredefinedTasks() {
        return this.tasks;
    }

    public final void addTask(GolfTask task) {
        Intrinsics.checkParameterIsNotNull(task, "addTask");
        this.tasks.add(task);
    }


    @JetClassObject
    public static final class object
            implements JetObject {

        public final GolfTaskManager getInstance() {
            GolfTaskManager tmp8_5 = ((GolfTaskManager) ServiceManager.getService(GolfTaskManager.class));
            if (tmp8_5 == null) throw new NullPointerException();
            return tmp8_5;
        }
    }
}