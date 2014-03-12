package org.jetbrains.codeGolf.plugin.recording;

import com.google.common.collect.Sets;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ActionController {
    private final Set<String> movingActions = Sets.newHashSet("EditorLeft", "EditorRight", "EditorDown", "EditorUp", "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown", "EditorPreviousWord", "EditorNextWord", "EditorScrollUp", "EditorScrollDown", "EditorTextStart", "EditorTextEnd", "EditorDownWithSelection", "EditorUpWithSelection", "EditorRightWithSelection", "EditorLeftWithSelection", "EditorLineStartWithSelection", "EditorLineEndWithSelection", "EditorPageDownWithSelection", "EditorPageUpWithSelection");
    private final Set<String> forbiddenActions = Sets.newHashSet("$Paste", "EditorPaste", "PasteMultiple", "EditorPasteSimple", "PlaybackLastMacro", "PlaySavedMacrosAction");
    private final Set<String> typingActions = Sets.newHashSet("EditorBackSpace");

    public boolean isMovingKey(int keyCode) {
        Set movingKeys = Sets.newHashSet(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP);
        return movingKeys.contains(keyCode);
    }

    public boolean isModifier(int keyCode) {
        List<Integer> modifiers = Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_META, KeyEvent.VK_SHIFT);
        return modifiers.contains(keyCode);
    }

    boolean isForbiddenAction(String actionId) {
        return forbiddenActions.contains(actionId);
    }

    boolean isMovingAction(String actionId) {
        return movingActions.contains(actionId);
    }

    public boolean isTypingAction(String actionId) {
        return typingActions.contains(actionId);
    }
}