package io.github.bertramn.soapui.env;

import com.eviware.soapui.impl.WorkspaceImpl;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.project.ProjectFactory;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.support.SoapUIException;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.io.InputStream;

public class ExtendedWsdlProjectFactory implements ProjectFactory<WsdlProject> {

  @Override
  public WsdlProject createNew() throws XmlException, IOException, SoapUIException {
    return new ExtendedWsdlProject();
  }

  @Override
  public WsdlProject createNew(String path) throws XmlException, IOException, SoapUIException {
    return new ExtendedWsdlProject(path);
  }

  @Override
  public WsdlProject createNew(String projectFile, String projectPassword) {
    return new ExtendedWsdlProject(projectFile, (WorkspaceImpl) null, true, null, projectPassword);
  }

  public WsdlProject createNew(Workspace workspace) {
    return new ExtendedWsdlProject((String) null, (WorkspaceImpl) workspace);
  }

  public WsdlProject createNew(String path, Workspace workspace) {
    return new ExtendedWsdlProject(path, (WorkspaceImpl) workspace);
  }

  public WsdlProject createNew(String path, Workspace workspace, boolean create) {
    return new ExtendedWsdlProject(path, (WorkspaceImpl) workspace, true, null, null);
  }

  public WsdlProject createNew(String path, Workspace workspace, boolean open, String tempName,
                               String projectPassword) {
    return new ExtendedWsdlProject(path, (WorkspaceImpl) workspace, open, tempName, projectPassword);
  }

  @Override
  public WsdlProject createNew(InputStream inputStream, WorkspaceImpl workspace) {
    return new ExtendedWsdlProject(inputStream, workspace);
  }

}
