package io.github.bertramn.soapui.env;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.DefaultEnvironment;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.components.JXToolBar;
import com.eviware.soapui.ui.support.DefaultDesktopPanel;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import org.apache.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.StringReader;

import static java.util.Optional.ofNullable;

public class EnvironmentDesktopPanel extends DefaultDesktopPanel {

  private static final Logger log = Logger.getLogger(EnvironmentDesktopPanel.class);

  private final WsdlProject project;

  private JComboBox selectEnvironmentComboBox;

  private RSyntaxTextArea textArea;

  private EnvironmentManager environmentManager;

  public EnvironmentDesktopPanel(@Nonnull WsdlProject project, @Nonnull EnvironmentManager environmentManager) {
    super("Environments", "Set project properties from external configuration.", new JPanel(new BorderLayout()));
    this.project = project;
    this.environmentManager = environmentManager;
    this.loadIcon("/io/github/bertramn/soapui/env/Environment.png");
    this.buildUI();
  }

  private void buildUI() {
    JPanel panel = (JPanel) getComponent();

    panel.add(buildToolbar(), "North");

    textArea = new RSyntaxTextArea(20, 60);
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
    textArea.setAnimateBracketMatching(true);
    textArea.setAntiAliasingEnabled(true);
    textArea.setCodeFoldingEnabled(true);
    textArea.setTabSize(3);
    textArea.setCaretPosition(0);
    textArea.requestFocusInWindow();
    textArea.setClearWhitespaceLinesEnabled(false);
    updateTextArea(project.getActiveEnvironment().getName(), project);
    RTextScrollPane scrollPane = new RTextScrollPane(textArea);
    panel.add(scrollPane, "Center");

    JPanel legend = new ButtonBarBuilder().getPanel();
    panel.add(legend, "South");

    panel.setPreferredSize(new Dimension(800, 600));

  }

  private JComponent buildToolbar() {
    JXToolBar toolbar = UISupport.createToolbar();
    toolbar.addSpace(5);
    toolbar.addLabeledFixed("Select Environment ", this.buildEnvironmentSelectBox());
    toolbar.addUnrelatedGap();
    toolbar.addFixed(new JLabel(" for project " + project.getName()));
    toolbar.addGlue();
    return toolbar;
  }

  private JComponent buildEnvironmentSelectBox() {

    DefaultComboBoxModel model = new DefaultComboBoxModel();
    model.addElement(DefaultEnvironment.NAME);
    for (String env : environmentManager.getEnvironmentNames()) {
      model.addElement(env);
    }

    String activeEnvironment = project.getActiveEnvironment().getName();
    if (environmentManager.getEnvironmentNames().contains(activeEnvironment)) {
      model.setSelectedItem(activeEnvironment);
    } else {
      model.setSelectedItem(DefaultEnvironment.NAME);
    }

    this.selectEnvironmentComboBox = new JComboBox(model);
    this.selectEnvironmentComboBox.addItemListener(new EnvironmentChangedListener(project));
    return this.selectEnvironmentComboBox;

  }

  private final void updateTextArea(String environmentName, WsdlProject project) {

    if (DefaultEnvironment.NAME.equals(environmentName)) {
      return;
    }

    String envYaml = ofNullable(environmentManager.getEnvironmentYaml(environmentName)).orElse("");

    try {
      textArea.read(new StringReader(envYaml), null);
    } catch (IOException e) {
      log.error("error: Failed to read environment YAML for project " + project.getName() + " environment " + environmentName, e);
    }

  }

  private class EnvironmentChangedListener implements ItemListener {

    private final WsdlProject project;

    public EnvironmentChangedListener(WsdlProject project) {
      this.project = project;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
      switch (e.getStateChange()) {
        case ItemEvent.SELECTED:
          if (e.getSource() instanceof JComboBox) {
            String selected = e.getItem().toString();
            log.info("selected environment is " + selected);
            if (DefaultEnvironment.NAME.equalsIgnoreCase(selected)) {
              resetEnvironment();
            } else {
              changeEnvironment(selected);
            }
          }
          break;
        default:
          break;
      }
    }

    private void changeEnvironment(String name) {
      project.setActiveEnvironment(new BasicEnvironment(name, project));
      EnvironmentDesktopPanel.this.updateTextArea(name, project);
    }

    private void resetEnvironment() {
      project.setActiveEnvironment(DefaultEnvironment.getInstance());
      textArea.setText("info: no data");
      textArea.setCaretPosition(0);
      textArea.discardAllEdits();
    }

  }


}
