package io.github.bertramn.soapui.env;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.plugins.ToolbarPosition;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;

@ActionConfiguration(
  actionGroup = "EnabledWsdlProjectActions",
  targetType = WsdlProject.class,
  toolbarPosition = ToolbarPosition.NONE,
  description = "Allows the configuration of an environment for the project.")
public class EnvironmentSelectAction extends AbstractSoapUIAction<WsdlProject> {

  private EnvironmentManager environmentManager;

  public EnvironmentSelectAction() {
    super("Environments", "Select the environment to use for the given project.");
    environmentManager = EnvironmentManagerHolder.getInstance();
  }

  @Override
  public void perform(WsdlProject project, Object o) {
    EnvironmentDesktopPanel panel = new EnvironmentDesktopPanel(project, environmentManager);
    UISupport.showDesktopPanel(panel);
    project.getWorkspace().addWorkspaceListener(environmentManager);
  }

}
