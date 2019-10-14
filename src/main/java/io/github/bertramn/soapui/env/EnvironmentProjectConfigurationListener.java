package io.github.bertramn.soapui.env;

import com.eviware.soapui.model.environment.Environment;
import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.support.ProjectListenerAdapter;
import com.eviware.soapui.plugins.ListenerConfiguration;

@ListenerConfiguration
public class EnvironmentProjectConfigurationListener extends ProjectListenerAdapter {

  @Override
  public void afterLoad(Project project) {
  }

  @Override
  public void environmentAdded(Environment env) {
  }

  @Override
  public void environmentSwitched(Environment env) {
    // TODO update components
  }

}
