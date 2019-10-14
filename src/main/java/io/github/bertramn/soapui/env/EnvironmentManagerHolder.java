package io.github.bertramn.soapui.env;

import java.io.File;

public class EnvironmentManagerHolder {

  private static final Object LOCK = new Object();

  private static EnvironmentManager INSTANCE;

  public static EnvironmentManager getInstance() {
    return getInstance(null);
  }

  public static EnvironmentManager getInstance(File file) {
    synchronized (LOCK) {
      if (INSTANCE == null) {
        INSTANCE = new EnvironmentManager(file);
      }
    }
    return INSTANCE;
  }

}
