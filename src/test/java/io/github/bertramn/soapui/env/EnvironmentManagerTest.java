package io.github.bertramn.soapui.env;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

public class EnvironmentManagerTest {

  private static final Logger log = LoggerFactory.getLogger(EnvironmentManagerTest.class);

  @Test
  public void loadConfiguration() {
    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-01.yml"));
    assertEquals("List of configured environment does not match", 4, em.getEnvironmentNames().size());
  }

  @Test
  public void testCheckEnvironmentPresent() {
    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-01.yml"));
    assertTrue(em.hasEnvironment("development"));
    assertEquals("dieter", em.getProperty("development", "username"));
  }

  @Test
  public void testCheckEnvironmentNotPresent() {
    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-02.yml"));
    assertFalse(em.hasEnvironment("development"));
    assertTrue(em.hasEnvironment("tst"));
    assertNull("Expecting no value", em.getProperty("development", "username"));
  }

  @Test
  public void testGetProperty() {
    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-01.yml"));
    assertEquals("dieter", em.getProperty("development", "username"));
  }

  @Test
  public void testGetDottedProperty() {
    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-01.yml"));
    assertEquals("snested", em.getProperty("staging", "flattened.nested.property"));
    assertEquals("dnested", em.getProperty("development", "flattened_nested_property"));
  }

  @Test
  public void testGetNestedProperty() {

    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-03.yml"));

    assertEquals("one-user", em.getProperty("tst", "subsystem.one.username"));
    assertEquals("5", em.getProperty("tst", "subsystem.one.level"));
    assertEquals("one-flattened-and-nested", em.getProperty("tst", "subsystem.one.flattened.property"));
    assertNull(em.getProperty("tst", "subsystem.one.not-exists"));
    assertNull(em.getProperty("tst", "subsystem.one.some_list"));
    assertEquals("fancy-one-user", em.getProperty("fancy production", "subsystem.one.username"));

  }

  @Test
  public void testEnvToYaml() {

    EnvironmentManager em = new EnvironmentManager(getTestFile("env-test-03.yml"));

    assertEquals("one-user", em.getProperty("tst", "subsystem.one.username"));
    String yaml = em.getEnvironmentYaml("tst");
    log.info("tst environment is:\n{}", yaml);

  }


  private File getTestFile(String name) {
    try {
      URL tr = getClass().getResource("/" + name);
      assertNotNull("Expecting test resource to be available", tr);
      return new File(tr.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Failed to load test file " + name, e);
    }
  }

}
