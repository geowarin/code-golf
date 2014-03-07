package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import javax.swing.Icon;
import jet.JetObject;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;


public final class StopSolvingAction extends AnAction
  implements JetObject
{
  private final ActionsRecorder recorder;


  public void actionPerformed( AnActionEvent e)
  {
    this.recorder.stopRecording();
  }


  public final ActionsRecorder getRecorder()
  {
    return this.recorder;
  }

  @JetConstructor
  public StopSolvingAction( ActionsRecorder recorder)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 45
    //   3: invokestatic 51	jet/runtime/Intrinsics:checkParameterIsNotNull	(Ljava/lang/Object;Ljava/lang/String;)V
    //   6: aload_0
    //   7: ldc 53
    //   9: ldc 55
    //   11: getstatic 61	com/intellij/icons/AllIcons$Actions:Suspend	Ljavax/swing/Icon;
    //   14: dup
    //   15: ldc 63
    //   17: ldc 64
    //   19: invokestatic 68	jet/runtime/Intrinsics:checkFieldIsNotNull	(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
    //   22: invokespecial 71	com/intellij/openapi/actionSystem/AnAction:<init>	(Ljava/lang/String;Ljava/lang/String;Ljavax/swing/Icon;)V
    //   25: aload_0
    //   26: aload_1
    //   27: putfield 28	org/jetbrains/codeGolf/plugin/StopSolvingAction:recorder	Lorg/jetbrains/codeGolf/plugin/ActionsRecorder;
    //   30: return
  }
}