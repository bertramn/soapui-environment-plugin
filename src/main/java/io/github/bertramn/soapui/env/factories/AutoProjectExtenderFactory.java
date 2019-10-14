package io.github.bertramn.soapui.env.factories;

import com.eviware.soapui.plugins.auto.factories.SimpleSoapUIFactory;
import io.github.bertramn.soapui.env.PluginProjectExtender;
import io.github.bertramn.soapui.env.ProjectExtenderFactory;

public class AutoProjectExtenderFactory extends SimpleSoapUIFactory<ProjectExtenderFactory> {

  public AutoProjectExtenderFactory(PluginProjectExtender annotation, Class<ProjectExtenderFactory> projectClass) {
    super(ProjectExtenderFactory.class, projectClass);
  }

}
