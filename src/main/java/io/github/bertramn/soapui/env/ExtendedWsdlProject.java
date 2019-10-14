package io.github.bertramn.soapui.env;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.*;
import com.eviware.soapui.impl.WorkspaceImpl;
import com.eviware.soapui.impl.rest.DefaultOAuth2ProfileContainer;
import com.eviware.soapui.impl.rest.OAuth1ProfileContainer;
import com.eviware.soapui.impl.rest.mock.RestMockService;
import com.eviware.soapui.impl.support.AbstractInterface;
import com.eviware.soapui.impl.wsdl.InterfaceFactoryRegistry;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.impl.wsdl.support.wss.DefaultWssContainer;
import com.eviware.soapui.settings.WsdlSettings;
import com.eviware.soapui.support.SoapUIException;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

public class ExtendedWsdlProject extends WsdlProject {

  public ExtendedWsdlProject() throws XmlException, IOException, SoapUIException {
  }

  public ExtendedWsdlProject(String path) throws XmlException, IOException, SoapUIException {
    super(path);
  }

  public ExtendedWsdlProject(String projectFile, String projectPassword) {
    super(projectFile, projectPassword);
  }

  public ExtendedWsdlProject(WorkspaceImpl workspace) {
    super(workspace);
  }

  public ExtendedWsdlProject(String path, WorkspaceImpl workspace) {
    super(path, workspace);
  }

  public ExtendedWsdlProject(String path, WorkspaceImpl workspace, boolean open, String tempName, String projectPassword) {
    super(path, workspace, open, tempName, projectPassword);
  }

  public ExtendedWsdlProject(InputStream inputStream, WorkspaceImpl workspace) {
    super(inputStream, workspace);
  }

  @Override
  public void loadProject(URL file) throws SoapUIException {
    super.loadProject(file);
  }

  protected void initializeActiveEnvironment() {
    String envName = this.getConfig().getActiveEnvironment();
    this.setActiveEnvironment(new BasicEnvironment(envName, this));
  }

  public SoapuiProjectDocumentConfig loadProjectFromInputStream(InputStream inputStream) throws XmlException, IOException, GeneralSecurityException {
    projectDocument = SoapuiProjectDocumentConfig.Factory.parse(inputStream);
    inputStream.close();

    // see if there is encoded data
    this.encryptionStatus = checkForEncodedData(projectDocument.getSoapuiProject());

    setConfig(projectDocument.getSoapuiProject());

    // removed cached definitions if caching is disabled
    if (!getSettings().getBoolean(WsdlSettings.CACHE_WSDLS)) {
      removeDefinitionCaches(projectDocument);
    }

    try {
      int majorVersion = Integer
        .parseInt(projectDocument.getSoapuiProject().getSoapuiVersion().split("\\.")[0]);
      if (majorVersion > Integer.parseInt(SoapUI.SOAPUI_VERSION.split("\\.")[0])) {
        log.warn("Project '" + projectDocument.getSoapuiProject().getName() + "' is from a newer version ("
          + projectDocument.getSoapuiProject().getSoapuiVersion() + ") of SoapUI than this ("
          + SoapUI.SOAPUI_VERSION + ") and parts of it may be incompatible or incorrect. "
          + "Saving this project with this version of SoapUI may cause it to function differently.");
      }
    } catch (Exception e) {
    }

    if (!getConfig().isSetProperties()) {
      getConfig().addNewProperties();
    }

    setPropertiesConfig(getConfig().getProperties());

    List<InterfaceConfig> interfaceConfigs = getConfig().getInterfaceList();
    for (InterfaceConfig config : interfaceConfigs) {
      AbstractInterface<?> iface = InterfaceFactoryRegistry.build(this, config);
      interfaces.add(iface);
    }

    List<TestSuiteConfig> testSuiteConfigs = getConfig().getTestSuiteList();
    for (TestSuiteConfig config : testSuiteConfigs) {
      testSuites.add(buildTestSuite(config));
    }

    List<MockServiceConfig> mockServiceConfigs = getConfig().getMockServiceList();
    for (MockServiceConfig config : mockServiceConfigs) {
      addWsdlMockService(new WsdlMockService(this, config));
    }

    List<RESTMockServiceConfig> restMockServiceConfigs = getConfig().getRestMockServiceList();
    for (RESTMockServiceConfig config : restMockServiceConfigs) {
      addRestMockService(new RestMockService(this, config));
    }

    if (!getConfig().isSetWssContainer()) {
      getConfig().addNewWssContainer();
    }

    wssContainer = new DefaultWssContainer(this, getConfig().getWssContainer());

    if (!getConfig().isSetOAuth2ProfileContainer()) {
      getConfig().addNewOAuth2ProfileContainer();
    }
    oAuth2ProfileContainer = new DefaultOAuth2ProfileContainer(this, getConfig().getOAuth2ProfileContainer());

    if (!getConfig().isSetOAuth1ProfileContainer()) {
      getConfig().addNewOAuth1ProfileContainer();
    }
    oAuth1ProfileContainer = new OAuth1ProfileContainer(this, getConfig().getOAuth1ProfileContainer());

    endpointStrategy.init(this);

    initializeActiveEnvironment();

    if (!getConfig().isSetAbortOnError()) {
      getConfig().setAbortOnError(false);
    }

    if (!getConfig().isSetRunType()) {
      getConfig().setRunType(TestSuiteRunTypesConfig.SEQUENTIAL);
    }

    afterLoad();

    return projectDocument;
  }

  @Override
  public boolean isEnvironmentMode() {
    return super.isEnvironmentMode();
  }
}
