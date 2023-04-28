package io.github.bertramn.soapui.env;

import com.eviware.soapui.impl.wsdl.WsdlProjectFactory;
import com.eviware.soapui.model.project.ProjectFactoryRegistry;
import com.eviware.soapui.model.propertyexpansion.PropertyExpander;
import com.eviware.soapui.model.util.PanelBuilderRegistry;
import com.eviware.soapui.plugins.PluginAdapter;
import com.eviware.soapui.plugins.PluginConfiguration;

@PluginConfiguration(
  groupId = "io.github.bertramn.soapui",
  name = "soapui-environment-plugin",
  version = "1.0.0",
  description = "Adds external environment management to SoapUI",
  infoUrl = "https://github.com/bertramn/soapui-environment-plugin.git")
public class EnvironmentPluginConfiguration extends PluginAdapter {

  public EnvironmentPluginConfiguration() {

    PropertyExpander.getDefaultExpander().addResolver(new EnvironmentPropertyResolver());
    ProjectFactoryRegistry.registrerProjectFactory(WsdlProjectFactory.WSDL_TYPE, new ExtendedWsdlProjectFactory());
    PanelBuilderRegistry.register(ExtendedWsdlProject.class, new ExtendedWsdlProjectPanelBuilder());
  }

}
