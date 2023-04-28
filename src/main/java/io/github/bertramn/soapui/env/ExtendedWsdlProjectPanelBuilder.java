package io.github.bertramn.soapui.env;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.panels.project.WsdlProjectDesktopPanel;
import com.eviware.soapui.support.components.JPropertiesTable;
import com.eviware.soapui.support.scripting.SoapUIScriptEngineRegistry;
import com.eviware.soapui.ui.desktop.DesktopPanel;

import javax.swing.*;

public class ExtendedWsdlProjectPanelBuilder extends EmptyPanelBuilder<ExtendedWsdlProject> {

  public ExtendedWsdlProjectPanelBuilder() {
  }

  public JPanel buildOverviewPanel(ExtendedWsdlProject project) {

    JPropertiesTable<WsdlProject> table = new JPropertiesTable<>("Project Properties", project);

    if (project.isOpen()) {
      table.addProperty("Name", "name", true);
      table.addProperty("Description", "description", true);
      table.addProperty("File", "path");

      if (!project.isDisabled()) {
        table.addProperty("Resource Root", "resourceRoot", new String[]{null, "${projectDir}", "${workspaceDir}"});
        table.addProperty("Cache Definitions", "cacheDefinitions", JPropertiesTable.BOOLEAN_OPTIONS);
        table.addPropertyShadow("Project Password", "shadowPassword", true);
        table.addProperty("Environment", "environment", false);
        table.addProperty("Script Language", "defaultScriptLanguage", SoapUIScriptEngineRegistry.getAvailableEngineIds());
        table.addProperty("Hermes Config", "hermesConfig", true);
      }

    } else {
      table.addProperty("File", "path");
    }

    return table;
  }

  public boolean hasOverviewPanel() {
    return true;
  }

  public boolean hasDesktopPanel() {
    return true;
  }

  public DesktopPanel buildDesktopPanel(WsdlProject modelItem) {
    return new WsdlProjectDesktopPanel(modelItem);
  }

}
