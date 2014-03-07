package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import javax.swing.Icon;
import jet.Function0;
import jet.JetObject;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Lcom/intellij/openapi/actionSystem/AnAction;", flags=16, abiVersion=6)
public final class TryAgainAction extends AnAction
  implements JetObject
{
  private final ActionsRecorder recorder;

  @JetMethod(returnType="V")
  public void actionPerformed(@JetValueParameter(name="e", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent e)
  {
    this.recorder.stopRecording();
    this.recorder.getRestarter().invoke();
  }

  @JetMethod(flags=17, propertyType="Lorg/jetbrains/codeGolf/plugin/ActionsRecorder;")
  public final ActionsRecorder getRecorder()
  {
    return this.recorder;
  }

  @JetConstructor
  public TryAgainAction(@JetValueParameter(name="recorder", type="Lorg/jetbrains/codeGolf/plugin/ActionsRecorder;") ActionsRecorder recorder)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 55
    //   3: invokestatic 61	jet/runtime/Intrinsics:checkParameterIsNotNull	(Ljava/lang/Object;Ljava/lang/String;)V
    //   6: aload_0
    //   7: ldc 63
    //   9: ldc 65
    //   11: getstatic 71	com/intellij/icons/AllIcons$Actions:Restart	Ljavax/swing/Icon;
    //   14: dup
    //   15: ldc 73
    //   17: ldc 74
    //   19: invokestatic 78	jet/runtime/Intrinsics:checkFieldIsNotNull	(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
    //   22: invokespecial 81	com/intellij/openapi/actionSystem/AnAction:<init>	(Ljava/lang/String;Ljava/lang/String;Ljavax/swing/Icon;)V
    //   25: aload_0
    //   26: aload_1
    //   27: putfield 28	org/jetbrains/codeGolf/plugin/TryAgainAction:recorder	Lorg/jetbrains/codeGolf/plugin/ActionsRecorder;
    //   30: return
  }
}