package io.github.bertramn.soapui.env;

import com.eviware.soapui.config.ServiceConfig;
import com.eviware.soapui.model.environment.Environment;
import com.eviware.soapui.model.environment.Property;
import com.eviware.soapui.model.environment.Service;
import com.eviware.soapui.model.project.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class BasicEnvironment implements Environment {

  private static final Logger log = LogManager.getLogger(BasicEnvironment.class);

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
    if (log.isDebugEnabled()) {
      log.debug("release environment {}", this.environmentId);
    }
  }

  @Override
  public Service addNewService(String name, ServiceConfig.Type.Enum anEnum) {
    if (log.isDebugEnabled()) {
      log.debug("add service {} to environment {}", name, this.environmentId);
    }
    return null;
  }

  @Override
  public void removeService(Service service) {
    if (log.isDebugEnabled()) {
      log.debug("remove service {} from environment {}", service.getName(), this.environmentId);
    }
  }

  @Override
  public Property addNewProperty(String name, String value) {
    if (log.isDebugEnabled()) {
      log.debug("add property {} to environment {}", name, this.environmentId);
    }
    return null;
  }

  @Override
  public void changePropertyName(String name, String value) {
    if (log.isDebugEnabled()) {
      log.debug("change property {} in environment {}", name, this.environmentId);
    }
  }

  @Override
  public void removeProperty(Property property) {
    if (log.isDebugEnabled()) {
      log.debug("remove property {} from environment {}", property.getName(), this.environmentId);
    }
  }

  @Override
  public void moveProperty(String name, int index) {
    if (log.isDebugEnabled()) {
      log.debug("change property {} oder", name);
    }
  }

}
