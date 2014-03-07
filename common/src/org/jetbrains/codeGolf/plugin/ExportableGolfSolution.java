package org.jetbrains.codeGolf.plugin;

import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Lorg/jetbrains/codeGolf/plugin/GolfSolution;", abiVersion=6)
public final class ExportableGolfSolution extends GolfSolution
  implements JetObject
{
  private String creationDate;
  private String taskName;

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getCreationDate()
  {
    return this.creationDate;
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final void setCreationDate(@JetValueParameter(name="<set-?>", type="Ljava/lang/String;") String <set-?>)
  {
    Intrinsics.checkParameterIsNotNull(<set-?>, "<set-creationDate>");
    this.creationDate = <set-?>;
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

  @JetConstructor
  public ExportableGolfSolution(@JetValueParameter(name="creationDate", hasDefaultValue=true, type="Ljava/lang/String;") String creationDate, @JetValueParameter(name="taskName", hasDefaultValue=true, type="Ljava/lang/String;") String taskName, @JetValueParameter(name="taskId", hasDefaultValue=true, type="Ljava/lang/String;") String taskId, @JetValueParameter(name="userLogin", hasDefaultValue=true, type="Ljava/lang/String;") String userLogin, @JetValueParameter(name="movingCount", hasDefaultValue=true, type="I") int movingCount, @JetValueParameter(name="typingCount", hasDefaultValue=true, type="I") int typingCount, @JetValueParameter(name="actionsCount", hasDefaultValue=true, type="I") int actionsCount, @JetValueParameter(name="usedActions", hasDefaultValue=true, type="Ljava/lang/String;") String usedActions)
  {
    super(taskId, userLogin, 
      movingCount, typingCount, actionsCount, 
      usedActions); this.creationDate = creationDate; this.taskName = taskName;
  }

  public ExportableGolfSolution(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, int paramInt3, String paramString5, int paramInt4)
  {
    this(paramString1, 
      paramString2, 
      paramString3, 
      paramString4, 
      paramInt1, 
      paramInt2, 
      paramInt3, 
      paramString5);
  }

  public ExportableGolfSolution()
  {
    this(null, null, null, null, 0, 0, 0, null, 255);
  }
}