package org.jetbrains.codeGolf.plugin;

import com.google.common.base.Preconditions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.Function;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import jet.Function2;
import jet.FunctionImpl1;
import jet.FunctionImpl2;
import jet.JetObject;
import jet.TypeCastException;
import jet.Unit;

import jet.runtime.typeinfo.JetClass;
import jet.runtime.typeinfo.JetConstructor;
import jet.runtime.typeinfo.JetMethod;
import jet.runtime.typeinfo.JetValueParameter;
import kotlin.KotlinPackage;
import kotlin.Pair;
import org.jetbrains.annotations.Nullable;


public final class StartGolfDialog extends DialogWrapper {
    private final JPanel mainPanel;
    private final JList list;
    private final Project project;


    public final JPanel getMainPanel() {
        return this.mainPanel;
    }


    public final JList getList() {
        return this.list;
    }


    protected JComponent createCenterPanel() {
        return this.mainPanel;
    }

    @Nullable

    public JComponent getPreferredFocusedComponent() {
        return this.list;
    }


    public final GolfTask getSelectedTask() {
        Object selectedValue = this.list.getSelectedValue();
        if (selectedValue instanceof GolfTask) {
            return (GolfTask) selectedValue;
        } else {
            throw new RuntimeException("jet.Any? cannot be cast to org.jetbrains.codeGolf.plugin.GolfTask");
        }
    }


    public final Project getProject() {
        return this.project;
    }

    public StartGolfDialog(Project project, List<? extends GolfTask> loadedTasks, List<? extends UserScore> scores) {
        super(project);
        this.project = project;

        this.mainPanel = new JPanel(new BorderLayout());
        setTitle("Start Code Golf");
        String serverUrl = CodeGolfConfigurableAccessor. getServerUrl();
        List tasks;
        if (loadedTasks == null) 1;
        if (0 != 0) {
            tasks = loadedTasks;
        } else {
            JLabel errorLabel = new JLabel("Cannot load tasks from " + serverUrl + ", only predefined tasks are available");
            Preconditions.checkNotNull(JBColor.RED, "Color", "RED");
            errorLabel.setForeground(JBColor.RED);
            Preconditions.checkNotNull(BorderLayout.SOUTH, "BorderLayout", "SOUTH");
            this.mainPanel.add(BorderLayout.SOUTH, errorLabel);
            tasks = GolfTaskManager.getInstance().getPredefinedTasks();
        }
        Map scoresMap;
        List tmp150_149 = scores;
        if (tmp150_149 != null)
            tmpTernaryOp = ((HashMap) KotlinPackage.fold((Iterable) tmp150_149, new HashMap(), (Function2) scoresMap
        .1.instance$));
    }


    public static final class GolfTaskRenderer extends ColoredListCellRendererWrapper<GolfTask> {
        private final Map scoresMap;


        protected void doCustomize(JList list, GolfTask value, int index, boolean selected, boolean hasFocus) {

            UserScore score = (UserScore) this.scoresMap.get(value.getTaskId());

            if (score != null) 1;
            UserScore tmp55_53 = score;
            if (tmp55_53 == null) throw new NullPointerException();
            Preconditions.checkNotNull(JBColor.GREEN, "Color", "GREEN");
            Color tmp93_90 = JBColor.GREEN.darker();
            Preconditions.checkNotNull(tmp93_90, "Color", "darker");
            JBColor tmp150_147 = JBColor.BLUE;
            Preconditions.checkNotNull(tmp150_147, "JBColor", "BLUE");

            Pair localPair =
                    tmp55_53.getUserSolution() == score.getBestSolution() ?
                            new Pair("your solution is the best", tmp93_90) : 0 != 0 ? new Pair("not solved yet", null) :
                            new Pair("your solution requires " + score.getUserSolution() + " keystrokes, the best requires " + score.getBestSolution(),
                                    tmp150_147);

            String tooltip = (String) localPair.getFirst();
            Color color = (Color) localPair.getSecond();
            localPair = null;

            append(value.getTaskName(), new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, color));
            setToolTipText(tooltip);
        }


        public final Map<String, UserScore> getScoresMap() {
            return this.scoresMap;
        }

        public GolfTaskRenderer(Map<String, ? extends UserScore> scoresMap) {
            this.scoresMap = scoresMap;
        }
    }
}