package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.icons.AllIcons;
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
        super("Diff", "show differences with solution", AllIcons.Actions.DiffWithCurrent);
        this.targetCode = targetCode;
        this.document = document;
    }
}