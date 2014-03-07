package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diff.DiffContent;
import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffRequest;
import com.intellij.openapi.diff.DiffTool;
import com.intellij.openapi.diff.SimpleContent;
import com.intellij.openapi.diff.SimpleDiffRequest;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import javax.swing.Icon;
import jet.JetObject;
import jet.runtime.Intrinsics;
import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;

@JetClass(signature="Lcom/intellij/openapi/actionSystem/AnAction;", flags=16, abiVersion=6)
public final class ShowDiffWithExpectedAction extends AnAction
  implements JetObject
{
  private final String targetCode;
  private final Document document;

  @JetMethod(returnType="V")
  public void actionPerformed(@JetValueParameter(name="e", type="?Lcom/intellij/openapi/actionSystem/AnActionEvent;") AnActionEvent e)
  {
    AnActionEvent tmp1_0 = e; if (tmp1_0 == null) Intrinsics.throwNpe();
    Project tmp11_8 = tmp1_0.getProject(); if (tmp11_8 == null) Intrinsics.throwNpe(); Project project = tmp11_8;
    SimpleDiffRequest diffData = new SimpleDiffRequest(project, "Difference");
    diffData.setContents((DiffContent)new SimpleContent(this.targetCode), (DiffContent)new SimpleContent(this.document.getText()));
    diffData.setContentTitles("Expected Code", "Actual Code");
    Object tmp79_76 = DiffTool.HINT_SHOW_FRAME; Intrinsics.checkFieldIsNotNull(tmp79_76, "DiffTool", "HINT_SHOW_FRAME"); diffData.addHint(tmp79_76);
    diffData.setGroupKey("#CodeGolfDiff");
    DiffManager tmp99_96 = DiffManager.getInstance(); if (tmp99_96 == null) Intrinsics.throwNpe();
    DiffTool tmp109_106 = tmp99_96.getIdeaDiffTool(); if (tmp109_106 == null) Intrinsics.throwNpe(); tmp109_106.show((DiffRequest)diffData);
  }

  @JetMethod(flags=17, propertyType="Ljava/lang/String;")
  public final String getTargetCode()
  {
    return this.targetCode;
  }

  @JetMethod(flags=17, propertyType="Lcom/intellij/openapi/editor/Document;")
  public final Document getDocument()
  {
    return this.document;
  }

  @JetConstructor
  public ShowDiffWithExpectedAction(@JetValueParameter(name="targetCode", type="Ljava/lang/String;") String targetCode, @JetValueParameter(name="document", type="Lcom/intellij/openapi/editor/Document;") Document document)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 132
    //   3: invokestatic 136	jet/runtime/Intrinsics:checkParameterIsNotNull	(Ljava/lang/Object;Ljava/lang/String;)V
    //   6: aload_2
    //   7: ldc 132
    //   9: invokestatic 136	jet/runtime/Intrinsics:checkParameterIsNotNull	(Ljava/lang/Object;Ljava/lang/String;)V
    //   12: aload_0
    //   13: ldc 138
    //   15: ldc 140
    //   17: getstatic 146	com/intellij/icons/AllIcons$Actions:DiffWithCurrent	Ljavax/swing/Icon;
    //   20: dup
    //   21: ldc 148
    //   23: ldc 149
    //   25: invokestatic 90	jet/runtime/Intrinsics:checkFieldIsNotNull	(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
    //   28: invokespecial 152	com/intellij/openapi/actionSystem/AnAction:<init>	(Ljava/lang/String;Ljava/lang/String;Ljavax/swing/Icon;)V
    //   31: aload_0
    //   32: aload_1
    //   33: putfield 50	org/jetbrains/codeGolf/plugin/ShowDiffWithExpectedAction:targetCode	Ljava/lang/String;
    //   36: aload_0
    //   37: aload_2
    //   38: putfield 59	org/jetbrains/codeGolf/plugin/ShowDiffWithExpectedAction:document	Lcom/intellij/openapi/editor/Document;
    //   41: return
  }
}