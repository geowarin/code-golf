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

@JetClass(signature="Ljava/lang/Object;", abiVersion=6)
public final class GolfTaskManager
  implements JetObject
{
  private final List tasks = (List)KotlinPackage.arrayListOf(new GolfTask[] { new GolfTask(null, null, "Hello World", "public class HelloWorld {}", 0, "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, world!\");\n    }\n}\n        ", null, 83) });
  public static final object object$ = new object();

  @JetMethod(flags=17, propertyType="Ljet/MutableList<Lorg/jetbrains/codeGolf/plugin/GolfTask;>;")
  public final List<GolfTask> getTasks()
  {
    return this.tasks;
  }

  @JetMethod(flags=16, returnType="Ljet/List<Lorg/jetbrains/codeGolf/plugin/GolfTask;>;")
  public final List<GolfTask> loadTasks(@JetValueParameter(name="serverUrl", type="Ljava/lang/String;") String serverUrl)
  {
    Intrinsics.checkParameterIsNotNull(serverUrl, "loadTasks"); ArrayList result = new ArrayList();
    List tmp18_15 = RestClientUtil.loadTasks(serverUrl); if (tmp18_15 != null) KotlinPackage.forEach((Iterable)tmp18_15, (Function1)new FunctionImpl1() { public final void invoke(GolfTask it) { Intrinsics.checkParameterIsNotNull(it, "<anonymous>"); this.$result.add(it); }  } ); 
  }

  @JetMethod(flags=16, returnType="Ljet/List<Lorg/jetbrains/codeGolf/plugin/UserScore;>;")
  public final List<UserScore> loadScores(@JetValueParameter(name="serverUrl", type="Ljava/lang/String;") String serverUrl, @JetValueParameter(name="username", type="Ljava/lang/String;") String username)
  {
    Intrinsics.checkParameterIsNotNull(serverUrl, "loadScores"); Intrinsics.checkParameterIsNotNull(username, "loadScores"); ArrayList result = new ArrayList();
    List tmp25_22 = RestClientUtil.loadScores(serverUrl, username); if (tmp25_22 != null) KotlinPackage.forEach((Iterable)tmp25_22, (Function1)new FunctionImpl1() { public final void invoke(UserScore it) { Intrinsics.checkParameterIsNotNull(it, "<anonymous>"); this.$result.add(it); }  } ); 
  }

  @JetMethod(flags=16, returnType="Ljet/List<Lorg/jetbrains/codeGolf/plugin/GolfTask;>;")
  public final List<GolfTask> getPredefinedTasks() {
    return this.tasks;
  }
  @JetMethod(flags=16, returnType="V")
  public final void addTask(@JetValueParameter(name="task", type="Lorg/jetbrains/codeGolf/plugin/GolfTask;") GolfTask task) { Intrinsics.checkParameterIsNotNull(task, "addTask"); this.tasks.add(task);
  }

  @JetClass(signature="Ljava/lang/Object;", flags=16, abiVersion=6)
  @JetClassObject
  public static final class object
    implements JetObject
  {
    @JetMethod(flags=16, returnType="Lorg/jetbrains/codeGolf/plugin/GolfTaskManager;")
    public final GolfTaskManager getInstance()
    {
      GolfTaskManager tmp8_5 = ((GolfTaskManager)ServiceManager.getService(GolfTaskManager.class)); if (tmp8_5 == null) Intrinsics.throwNpe(); return tmp8_5;
    }
  }
}