# SoapUI Environment Plugin

This plugin extends the default SoapUI project and adds capabilities to inject properties that are managed outside the SoapUI workspace. This feature works similar to the [String Boot Dev Tools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools) just without the sophistication.

## Installation

These instructions work with SoapUI 5.7.0. For older versions see prior commits. 

1. Create a `~/.soapuios/plugins` folder

2. Download the [`soapui-environment-plugin-1.0.1.jar`](https://github.com/bertramn/soapui-environment-plugin/releases/download/v1.0.1-alpha/soapui-environment-plugin-1.0.1.jar) and copy to `~/.soapuios/plugins` folder

3. Download dependencies and copy them into the `${SOAPUI_HOME}/lib` folder

    * [snakeyaml-2.0.jar](https://search.maven.org/remotecontent?filepath=org/yaml/snakeyaml/2.0/snakeyaml-2.0.jar)
    * [rsyntaxtextarea-3.3.3.jar](https://search.maven.org/remotecontent?filepath=com/fifesoft/rsyntaxtextarea/3.3.3/rsyntaxtextarea-3.3.3.jar)
 
    ```sh
    cp ~/Downloads/snakeyaml-2.0.jar /Applications/SoapUI-5.7.0.app/Contents/java/app/lib/.
    cp ~/Downloads/rsyntaxtextarea-3.3.3.jar /Applications/SoapUI-5.7.0.app/Contents/java/app/lib/.
    ```

**IMPORTANT**: SoapUI 5.7.0 contains 2 similar but different XMLBeans implementations. You will need to delete `xmlbeans-xpath-2.6.0.jar` from the `${SOAPUI_HOME}/lib` folder. The `xmlbeans-3.1.1-sb-fixed.jar` is the only one that should be present. TL;DR having both will screw up your XQueries and spotting the root cause when things fail is hard. 

  ```sh
  rm -f /Applications/SoapUI-5.7.0.app/Contents/java/app/lib/xmlbeans-xpath-2.6.0.jar
  ```


## External Configuration File

To inject external variables into the SoapUI project, create a `.soapui-env.yml` file and add content. The plugin will interpret the first level attributes as environment name and anything underneath will be expandable properties.

Example:

```yaml
---
dev:
  subsystem:
    one:
      username: one-dev-user
      level: 5
    two:
      username: two-dev-user
tst:
  subsystem:
    one:
      username: one-test-user
      level: 6
    two:
      username: two--test-user
'fancy environment':
  subsystem:
    one:
      username: fancy-three-user
```

In SoapUI you can now select environments `[dev|tst|fancy environment]` and within each you can for instance use `${subsystem.one.username}` as property replacement.

The variables are also available to groovy scripts with expand.

```groovy
def username = context.expand( '${subsystem.one.username}' )
```
