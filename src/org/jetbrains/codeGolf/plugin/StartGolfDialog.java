package org.jetbrains.codeGolf.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
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


public final class StartGolfDialog extends DialogWrapper
        implements JetObject {
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
        return (JComponent) this.mainPanel;
    }

    @Nullable

    public JComponent getPreferredFocusedComponent() {
        return (JComponent) this.list;
    }


    public final GolfTask getSelectedTask() {
        Object tmp7_4 = this.list.getSelectedValue();
        if (tmp7_4 == null)
            throw new TypeCastException("jet.Any? cannot be cast to org.jetbrains.codeGolf.plugin.GolfTask");
        return (GolfTask) tmp7_4;
    }


    public final Project getProject() {
        return this.project;
    }

    @JetConstructor
    public StartGolfDialog(Project project, List<? extends GolfTask> loadedTasks, List<? extends UserScore> scores) {
        super(project);
        this.project = project;

        this.mainPanel = new JPanel((LayoutManager) new BorderLayout());
        setTitle("Start Code Golf");
        String serverUrl = CodeGolfConfigurableAccessor. getServerUrl();
        List tasks;
        if (loadedTasks == null) 1;
        if (0 != 0) {
            tasks = loadedTasks;
        } else {
            JLabel errorLabel = new JLabel("Cannot load tasks from " + serverUrl + ", only predefined tasks are available");
            Color tmp103_100 = Color.RED;
            Preconditions.checkNotNull(tmp103_100, "Color", "RED");
            errorLabel.setForeground(tmp103_100);
            String tmp121_118 = BorderLayout.SOUTH;
            Preconditions.checkNotNull(tmp121_118, "BorderLayout", "SOUTH");
            this.mainPanel.add(tmp121_118, (Component) errorLabel);
            tasks = GolfTaskManager.object$.getInstance().getPredefinedTasks();
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
            if (value != null) 1;
            if (0 != 0) return;
            UserScore score = (UserScore) this.scoresMap.get(value.getTaskId());

            if (score != null) 1;
            UserScore tmp55_53 = score;
            if (tmp55_53 == null) throw new NullPointerException();
            Color tmp82_79 = Color.GREEN;
            Preconditions.checkNotNull(tmp82_79, "Color", "GREEN");
            Color tmp93_90 = tmp82_79.darker();
            Preconditions.checkNotNull(tmp93_90, "Color", "darker");
            JBColor tmp150_147 = JBColor.BLUE;
            Preconditions.checkNotNull(tmp150_147, "JBColor", "BLUE");

            Pair localPair =
                    tmp55_53.getUserSolution() == score.getBestSolution() ?
                            new Pair("your solution is the best", tmp93_90) : 0 != 0 ? new Pair("not solved yet", null) :
                            new Pair("your solution requires " + score.getUserSolution() + " keystrokes, the best requires " + score.getBestSolution(),
                                    tmp150_147);

            String tooltip = (String) localPair.component1();
            Color color = (Color) localPair.component2();
            localPair = null;

            append(value.getTaskName(), new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, color));
            setToolTipText(tooltip);
        }


        public final Map<String, UserScore> getScoresMap() {
            return this.scoresMap;
        }

        @JetConstructor
        public GolfTaskRenderer(Map<String, ? extends UserScore> scoresMap) {
            this.scoresMap = scoresMap;
        }
    }
}