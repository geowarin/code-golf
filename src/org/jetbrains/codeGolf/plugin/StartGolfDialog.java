package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.codeGolf.plugin.settings.CodeGolfSettings;
import org.jetbrains.codeGolf.plugin.task.GolfTaskManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class StartGolfDialog extends DialogWrapper {
    private final JPanel mainPanel;
    private final JList list;
    private final Project project;

    public StartGolfDialog(Project project, List<GolfTask> loadedTasks, List<UserScore> scores) {
        super(project);
        this.project = project;

        mainPanel = new JPanel(new BorderLayout());
        setTitle("Start Code Golf");
        List tasks;
        if (loadedTasks != null) {
            tasks = loadedTasks;
        } else {
            String serverUrl = CodeGolfSettings.getServerUrl();
            JLabel errorLabel = new JLabel("Cannot load tasks from " + serverUrl + ", only predefined tasks are available");
            errorLabel.setForeground(JBColor.RED);
            mainPanel.add(BorderLayout.SOUTH, errorLabel);
            tasks = GolfTaskManager.getInstance().getPredefinedTasks();
        }
        Map<String, UserScore> scoresMap = new HashMap<String, UserScore>();
        for (UserScore score : scores) {
            scoresMap.put(score.getTaskId(), score);
        }

        list = new JBList(tasks);
        list.setCellRenderer(new GolfTaskRenderer(scoresMap));
        mainPanel.add(new JLabel("Select Task:"), BorderLayout.NORTH);
        mainPanel.add(new JBScrollPane(list), BorderLayout.CENTER);
        mainPanel.setPreferredSize(new Dimension(400, 400));
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return list;
    }

    @Nullable
    public final GolfTask getSelectedTask() {
        return (GolfTask) list.getSelectedValue();
    }

    public final Project getProject() {
        return project;
    }

    public static final class GolfTaskRenderer extends ColoredListCellRendererWrapper<GolfTask> {
        private final Map<String, UserScore> scoresMap;

        @Override
        protected void doCustomize(JList list, GolfTask value, int index, boolean selected, boolean hasFocus) {

            UserScore score = scoresMap.get(value.getTaskId());

            Pair<String, Color> localPair;
            if (score == null) {
                localPair = new Pair<String, Color>("not solved yet", null);
            } else {
                localPair = score.getUserSolution() == score.getBestSolution() ?
                        new Pair<String, Color>("your solution is the best", JBColor.GREEN.darker()) :
                        new Pair<String, Color>("your solution requires " + score.getUserSolution() + " keystrokes, the best requires " + score.getBestSolution(), JBColor.BLUE);
            }

            String tooltip = localPair.getFirst();
            Color color = localPair.getSecond();

            append(value.getTaskName(), new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, color));
            setToolTipText(tooltip);
        }

        public GolfTaskRenderer(Map<String, UserScore> scoresMap) {
            this.scoresMap = scoresMap;
        }
    }
}