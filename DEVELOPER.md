# Developer Notes

## Files Of Interest

MacOS dmg installer:

```sh
export SOAPUI_VERSION=5.7.0
export SOAPUI_HOME=/Applications/SoapUI-${SOAPUI_VERSION}.app/Contents/
```

`${SOAPUI_HOME}/vmoptions.txt`

Configuration file processed by the app launcher to pass arguments to the Java Virtual Machine.

`${SOAPUI_HOME}/java/app`

The SoapUI application directory

`${SOAPUI_HOME}/PlugIns/jre.bundle/Contents/Home`

The shipped Java JRE directory


## Debugging

Edit `${SOAPUI_HOME}/Resources/app/bin/soapui.sh` and add the variable `$DEBUG_OPTS` to the line that defines `JAVA_OPTS`.

```shell
...
SOAPUI_CLASSPATH=$JFXRTPATH:$SOAPUI_CLASSPATH

export SOAPUI_CLASSPATH

# add the debug hook to the beginning of the variable
JAVA_OPTS="$DEBUG_OPTS -Xms128m -Xmx1024m -XX:MinHeapFreeRatio=20 ..."
```
Then setup a remote debug session

```shell
# configure custom logging
DEBUG_OPTS="-Dlog4j.configuration=${sys:soapui.home}/soapui-log4j.xml"

# configure startup with remote debugger attachment
export DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"
```


