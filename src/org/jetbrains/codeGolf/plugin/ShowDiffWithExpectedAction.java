package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffTool;
import com.intellij.openapi.diff.SimpleContent;
import com.intellij.openapi.diff.SimpleDiffRequest;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;


public final class ShowDiffWithExpectedAction extends AnAction {
    private final String targetCode;
    private final Document document;


    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) throw new NullPointerException();

        SimpleDiffRequest diffData = new SimpleDiffRequest(project, "Difference");
        diffData.setContents(new SimpleContent(this.targetCode), new SimpleContent(this.document.getText()));
        diffData.setContentTitles("Expected Code", "Actual Code");
        Preconditions.checkNotNull(DiffTool.HINT_SHOW_FRAME, "DiffTool", "HINT_SHOW_FRAME");
        diffData.addHint(DiffTool.HINT_SHOW_FRAME);
        diffData.setGroupKey("#CodeGolfDiff");

        DiffManager diffManager = DiffManager.getInstance();
        if (diffManager == null) throw new NullPointerException();

        DiffTool ideaDiffTool = diffManager.getIdeaDiffTool();
        if (ideaDiffTool == null) throw new NullPointerException();

        ideaDiffTool.show(diffData);
    }


    public final String getTargetCode() {
        return this.targetCode;
    }


    public final Document getDocument() {
        return this.document;
    }

    public ShowDiffWithExpectedAction(String targetCode, Document document) {
        this.targetCode = targetCode;
        this.document = document;
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