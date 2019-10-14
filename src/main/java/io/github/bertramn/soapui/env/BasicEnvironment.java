package io.github.bertramn.soapui.env;

import com.eviware.soapui.config.ServiceConfig;
import com.eviware.soapui.model.environment.Environment;
import com.eviware.soapui.model.environment.Property;
import com.eviware.soapui.model.environment.Service;
import com.eviware.soapui.model.project.Project;

import javax.annotation.Nonnull;

public class BasicEnvironment implements Environment {

  private String environmentId;

  private Project project;

  public BasicEnvironment(String environmentId, Project project) {
    this.environmentId = environmentId;
    this.project = project;
  }

  @Override
  public String getName() {
    return environmentId;
  }

  @Override
  public void setName(@Nonnull String environmentId) {
    this.environmentId = environmentId;
  }

  @Override
  public Project getProject() {
    return project;
  }

  @Override
  public void setProject(@Nonnull Project project) {
    this.project = project;
  }

  @Override
  public void release() {

  }

  @Override
  public Service addNewService(String name, ServiceConfig.Type.Enum anEnum) {
    return null;
  }

  @Override
  public void removeService(Service service) {

  }

  @Override
  public Property addNewProperty(String name, String value) {
    return null;
  }

  @Override
  public void changePropertyName(String name, String value) {

  }

  @Override
  public void removeProperty(Property property) {

  }

  @Override
  public void moveProperty(String name, int index) {

  }

}
