package org.jetbrains.codeGolf.plugin;

import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Ljava/lang/Object;", abiVersion=6)
public final class GolfTask
  implements JetObject
{
  private String authorLogin;
  private String taskId;
  private String taskName;
  private String initialCode;
  private int initialOffset;
  private String targetCode;
  private String hint;

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getAuthorLogin()
  {
    return this.authorLogin;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setAuthorLogin(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-authorLogin>");
    this.authorLogin = <set-?>;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getTaskId()
  {
    return this.taskId;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setTaskId(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-taskId>");
    this.taskId = <set-?>;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getTaskName()
  {
    return this.taskName;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setTaskName(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-taskName>");
    this.taskName = <set-?>;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getInitialCode()
  {
    return this.initialCode;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setInitialCode(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-initialCode>");
    this.initialCode = <set-?>;
  }

  @JetMethod(flags=17, propertyType="I")
  public final int getInitialOffset()
  {
    return this.initialOffset;
  }

  @JetMethod(flags=17, propertyType="I")
  public final void setInitialOffset(@JetValueParameter(name="<set-?>", type="I") int <set-?>)
  {
    this.initialOffset = <set-?>;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getTargetCode()
  {
    return this.targetCode;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setTargetCode(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-targetCode>");
    this.targetCode = <set-?>;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getHint()
  {
    return this.hint;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setHint(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-hint>");
    this.hint = <set-?>;
  }

  @JetConstructor
  public GolfTask(@JetValueParameter(name="authorLogin", hasDefaultValue=true, type="Ljava/lang/String;") String authorLogin, @JetValueParameter(name="taskId", hasDefaultValue=true, type="Ljava/lang/String;") String taskId, @JetValueParameter(name="taskName", hasDefaultValue=true, type="Ljava/lang/String;") String taskName, @JetValueParameter(name="initialCode", hasDefaultValue=true, type="Ljava/lang/String;") String initialCode, @JetValueParameter(name="initialOffset", hasDefaultValue=true, type="I") int initialOffset, @JetValueParameter(name="targetCode", hasDefaultValue=true, type="Ljava/lang/String;") String targetCode, @JetValueParameter(name="hint", hasDefaultValue=true, type="Ljava/lang/String;") String hint)
  {
    this.authorLogin = authorLogin;
    this.taskId = taskId;
    this.taskName = taskName;
    this.initialCode = initialCode;
    this.initialOffset = initialOffset;
    this.targetCode = targetCode;
    this.hint = hint;
  }

  public GolfTask(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, String paramString5, String paramString6, int paramInt2)
  {
    this(paramString1, 
      paramString2, 
      paramString3, 
      paramString4, 
      paramInt1, 
      paramString5, 
      paramString6);
  }

  public GolfTask()
  {
    this(null, null, null, null, 0, null, null, 127);
  }
}