package io.github.bertramn.soapui.env;

import com.eviware.soapui.model.project.Project;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.model.workspace.WorkspaceListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EnvironmentManager implements WorkspaceListener {

  private static final Logger log = LogManager.getLogger(EnvironmentManager.class);

  private final Map<String, Map<String, Object>> environments = new HashMap<>();

  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("env-file-watcher").build());

  private final Buffer watchEvents = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(3));

  private final Yaml yaml;

  public EnvironmentManager() {
    this(null);
  }

  public EnvironmentManager(@Nullable File file) {

    DumperOptions options = new DumperOptions();
    options.setIndent(2);
    options.setPrettyFlow(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    yaml = new Yaml(options);


    if (file == null) {
      file = new File(System.getProperty("user.home"), ".soapui-env.yml");
    }

    Path configurationFile = Paths.get(file.toURI());

    if (!Files.isRegularFile(configurationFile)) {
      log.warn(configurationFile.toString() + " file does not exist, skip loading environment configuration");
    } else {
      loadConfiguration(configurationFile);
      createWatch(configurationFile);
    }

  }

  private void loadConfiguration(@Nonnull final Path file) {

    try {
      Map<String, Map<String, Object>> values = yaml.load(new FileInputStream(file.toFile()));
      environments.clear();
      if(values != null) {
        environments.putAll(values);
      }
    } catch (FileNotFoundException e) {
      log.error("Failed to load " + file.toString(), e);
    }

  }

  private void createWatch(final Path file) {

    Runnable watch = () -> {

      Path folder = file.getParent();

      try (WatchService watcher = folder.getFileSystem().newWatchService()) {
        folder.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey watchKey;
        while (true) {
          watchKey = watcher.take();
          if (watchKey != null) {
            watchKey
              .pollEvents()
              .stream()
              .map(WatchEvent::context)
              .filter(Objects::nonNull) // exclude nulls
              .filter(Path.class::isInstance) // check context is a path
              .map(Path.class::cast)
              .filter(p -> file.getFileName().equals(p.getFileName())) // only look at file we monitor
              .forEach(p -> EnvironmentManager.this.watchEvents.add(file));  // add to watch events for downstream processing
            watchKey.reset();
          }
        }
      } catch (IOException e) {
        log.error("Failed to register watch for " + file.toString(), e);
      } catch (InterruptedException ie) {
        log.info(ie.getMessage());
      } finally {
        log.info("finished watching " + file.toString());
      }
    };

    Runnable delay = () -> {
      while (true) {
        if (!watchEvents.isEmpty()) {
          Path updatedFile = (Path) watchEvents.get();
          watchEvents.clear();
          EnvironmentManager.this.loadConfiguration(updatedFile);
          log.info("processed watch queue");
          try {
            Thread.sleep(10000);
          } catch (InterruptedException ignore) {
          }
        }
      }
    };

    executor.submit(watch);
    executor.schedule(delay, 1, TimeUnit.SECONDS);

  }

  public List<String> getEnvironmentNames() {
    return new ArrayList<>(environments.keySet());
  }

  public boolean hasEnvironment(String name) {
    return environments.containsKey(name);
  }

  @Nullable
  public String getProperty(@Nonnull String environment, @Nonnull String path) {

    if ("".equals(path.trim())) {
      return null;
    }

    if (!hasEnvironment(environment)) {
      return null;
    }

    Map<String, Object> env = environments.get(environment);

    if (env == null || env.isEmpty()) {
      return null;
    }

    return getNestedProperty(env, path, 0);

  }

  private String getNestedProperty(@Nonnull Map<String, Object> map, @Nonnull String path, int level) {

    if (map.containsKey(path)) {
      Object prop = map.get(path);
      return objToString(prop);
    } else if (map.containsKey(path.replace('.', '_'))) {
      Object prop = map.get(path.replace('.', '_'));
      return objToString(prop);
    } else {
      int pathSep = path.indexOf('.');
      // no more nesting to look into
      if (pathSep == -1) {
        return null;
      }
      String propName = path.substring(0, pathSep);
      String subPath = path.substring(pathSep + 1);
      Object prop = map.get(propName);
      if (prop instanceof Map) {
        return getNestedProperty((Map<String, Object>) prop, subPath, level + 1);
      } else {
        return objToString(prop);
      }
    }

  }

  private String objToString(@Nullable Object o) {
    if (o instanceof String) {
      return (String) o;
    } else if (o instanceof Integer || o instanceof Long || o instanceof Boolean || o instanceof Double) {
      return o.toString();
    } else {
      return null;
    }
  }

  public String getEnvironmentYaml(@Nonnull String environment) {

    if (!hasEnvironment(environment)) {
      return null;
    }

    Map<String, Object> env = environments.get(environment);

    if (env == null || env.isEmpty()) {
      return null;
    }

    return yaml.dump(env);

  }

  // region soapui events

  @Override
  public void projectAdded(Project project) {
    log.info("project " + project.getName() + " added");
  }

  @Override
  public void projectRemoved(Project project) {
    log.info("project " + project.getName() + " removed");
  }

  @Override
  public void projectChanged(Project project) {
    log.info("project " + project.getName() + " changed");
  }

  @Override
  public void projectOpened(Project project) {
    log.info("project " + project.getName() + " opened");
  }

  @Override
  public void projectClosed(Project project) {
    log.info("project " + project.getName() + " closed");
  }

  @Override
  public void workspaceSwitching(Workspace workspace) {
    log.info("workspace " + workspace.getName() + " pre switch");
  }

  @Override
  public void workspaceSwitched(Workspace workspace) {
    log.info("workspace " + workspace.getName() + " post switch");
  }

  // endregion

}
