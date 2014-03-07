package org.jetbrains.codeGolf.plugin;

import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Ljava/lang/Object;", abiVersion=6)
public final class UserScore
  implements JetObject
{
  private String taskId;
  private int userSolution;
  private int bestSolution;
  private double score;

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

  @JetMethod(flags=17, propertyType="I")
  public final int getUserSolution()
  {
    return this.userSolution;
  }

  @JetMethod(flags=17, propertyType="I")
  public final void setUserSolution(@JetValueParameter(name="<set-?>", type="I") int <set-?>)
  {
    this.userSolution = <set-?>;
  }

  @JetMethod(flags=17, propertyType="I")
  public final int getBestSolution()
  {
    return this.bestSolution;
  }

  @JetMethod(flags=17, propertyType="I")
  public final void setBestSolution(@JetValueParameter(name="<set-?>", type="I") int <set-?>)
  {
    this.bestSolution = <set-?>;
  }

  @JetMethod(flags=17, propertyType="D")
  public final double getScore()
  {
    return this.score;
  }

  @JetMethod(flags=17, propertyType="D")
  public final void setScore(@JetValueParameter(name="<set-?>", type="D") double <set-?>)
  {
    this.score = <set-?>;
  }

  @JetConstructor
  public UserScore(@JetValueParameter(name="taskId", hasDefaultValue=true, type="Ljava/lang/String;") String taskId, @JetValueParameter(name="userSolution", hasDefaultValue=true, type="I") int userSolution, @JetValueParameter(name="bestSolution", hasDefaultValue=true, type="I") int bestSolution, @JetValueParameter(name="score", hasDefaultValue=true, type="D") double score)
  {
    this.taskId = taskId;
    this.userSolution = userSolution;
    this.bestSolution = bestSolution;
    this.score = score;
  }

  public UserScore(String paramString, int paramInt1, int paramInt2, double paramDouble, int paramInt3)
  {
    this(paramString, 
      paramInt1, 
      paramInt2, 
      paramDouble);
  }

  public UserScore()
  {
    this(null, 0, 0, 0.0D, 15);
  }
}