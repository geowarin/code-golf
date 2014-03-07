package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;


public final class NavigateToEditorAction extends AnAction {
    private final Document document;

    public void actionPerformed(AnActionEvent e) {
        // TODO
//        VirtualFile file;
//        FileDocumentManager tmp3_0 = FileDocumentManager.getInstance();
//        if (tmp3_0 != null) tmpTernaryOp = tmp3_0.getFile(this.document);
    }


    public final Document getDocument() {
        return this.document;
    }

    public NavigateToEditorAction(Document document) {
        // Byte code:
        //   0: aload_1
        //   1: ldc 84
        //   3: invokestatic 88	jet/runtime/Intrinsics:checkParameterIsNotNull	(Ljava/lang/Object;Ljava/lang/String;)V
        //   6: aload_0
        //   7: ldc 90
        //   9: ldc 92
        //   11: getstatic 98	com/intellij/icons/AllIcons$General:AutoscrollToSource	Ljavax/swing/Icon;
        //   14: dup
        //   15: ldc 100
        //   17: ldc 101
        //   19: invokestatic 105	jet/runtime/Intrinsics:checkFieldIsNotNull	(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
        //   22: invokespecial 108	com/intellij/openapi/actionSystem/AnAction:<init>	(Ljava/lang/String;Ljava/lang/String;Ljavax/swing/Icon;)V
        //   25: aload_0
        //   26: aload_1
        //   27: putfield 34	org/jetbrains/codeGolf/plugin/NavigateToEditorAction:document	Lcom/intellij/openapi/editor/Document;
        //   30: return
        this.document = document;
    }
}