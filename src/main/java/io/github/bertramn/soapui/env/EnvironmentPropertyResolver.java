package io.github.bertramn.soapui.env;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.environment.Environment;
import com.eviware.soapui.model.propertyexpansion.PropertyExpansionContext;
import com.eviware.soapui.model.propertyexpansion.resolvers.PropertyResolver;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.plugins.auto.PluginPropertyResolver;
import com.google.inject.Guice;
import org.apache.log4j.Logger;

@PluginPropertyResolver
public class EnvironmentPropertyResolver implements PropertyResolver {

  private final static Logger log = Logger.getLogger(EnvironmentPropertyResolver.class);

  EnvironmentManager environmentManager;

  public EnvironmentPropertyResolver() {
    environmentManager = EnvironmentManagerHolder.getInstance();
  }

  @Override
  public String resolveProperty(PropertyExpansionContext context, String name, boolean globalOverride) {

    if (context instanceof TestCaseRunContext) {
      TestCaseRunContext ctx = (TestCaseRunContext) context;
      WsdlProject project = (WsdlProject) ctx.getTestCase().getProject();
      Environment env = project.getActiveEnvironment();
      if (environmentManager.hasEnvironment(env.getName())) {
        return environmentManager.getProperty(env.getName(), name);
      }
    } else {
      log.error("resolve " + name + " in context " + context.toString());
    }

    return null;
  }

}
