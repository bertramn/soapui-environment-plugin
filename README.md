# SoapUI Environment Plugin

This plugin extends the default project and adds capabilities

## Installation

1. Create a `~/.soapuios/plugins` folder

2. Download the [`soapui-environment-plugin-1.0.0.jar`](https://github.com/bertramn/soapui-environment-plugin/releases/download/v1.0.0-alpha/soapui-environment-plugin-1.0.0.jar) and copy to `~/.soapuios/plugins` folder

3. Download dependencies and copy to into `${SOAPUI_HOME}/lib` folder

    * [snakeyaml-1.17.jar](https://search.maven.org/remotecontent?filepath=org/yaml/snakeyaml/1.17/snakeyaml-1.17.jar)
    * [rsyntaxtextarea-3.0.4.jar](https://search.maven.org/remotecontent?filepath=com/fifesoft/rsyntaxtextarea/3.0.4/rsyntaxtextarea-3.0.4.jar)

    or install dependencies from local maven cache
      
    ```sh
    cp ~/.m2/repository/org/yaml/snakeyaml/1.17/snakeyaml-1.17.jar /Applications/SoapUI-5.5.0.app/Contents/java/app/lib/.
    cp ~/.m2/repository/com/fifesoft/rsyntaxtextarea/3.0.4/rsyntaxtextarea-3.0.4.jar /Applications/SoapUI-5.5.0.app/Contents/java/app/lib/.
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
