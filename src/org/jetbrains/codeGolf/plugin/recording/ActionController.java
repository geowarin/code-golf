package org.jetbrains.codeGolf.plugin.recording;

import com.google.common.collect.Sets;

import java.util.Set;

public class ActionController {
    private final Set<String> movingActions = Sets.newHashSet("EditorLeft", "EditorRight", "EditorDown", "EditorUp", "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown", "EditorPreviousWord", "EditorNextWord", "EditorScrollUp", "EditorScrollDown", "EditorTextStart", "EditorTextEnd", "EditorDownWithSelection", "EditorUpWithSelection", "EditorRightWithSelection", "EditorLeftWithSelection", "EditorLineStartWithSelection", "EditorLineEndWithSelection", "EditorPageDownWithSelection", "EditorPageUpWithSelection");
    private final Set<String> forbiddenActions = Sets.newHashSet("$Paste", "EditorPaste", "PasteMultiple", "EditorPasteSimple", "PlaybackLastMacro", "PlaySavedMacrosAction");
    private final Set<String> typingActions = Sets.newHashSet("EditorBackSpace");

    boolean isForbiddenAction(ActionsRecorder actionsRecorder, String actionId) {
        return forbiddenActions.contains(actionId);
    }

    boolean isMovingAction(String actionId) {
        return movingActions.contains(actionId);
    }

    public boolean isTypingAction(String actionId) {
        return typingActions.contains(actionId);
    }
}