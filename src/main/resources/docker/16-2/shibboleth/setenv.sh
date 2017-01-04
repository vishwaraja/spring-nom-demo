#!/bin/sh
CLASSPATH=$CATALINA_BASE/libs/jul-to-slf4j-1.7.7.jar:\
$CATALINA_BASE/libs/slf4j-api-1.7.7.jar:\
$CATALINA_BASE/libs/logback-classic-1.1.3.jar:\
$CATALINA_BASE/libs/logback-core-1.1.3.jar:\
$CATALINA_BASE/libs/serializer-2.10.0.jar:\
$CATALINA_BASE/libs/xalan-2.7.1.jar:\
$CATALINA_BASE/libs/xercesImpl-2.10.0.jar:\
$CATALINA_BASE/libs/xml-apis-2.10.0.jar:\
$CATALINA_BASE/libs/xml-resolver-1.2.jar:\
$NOM_EPORTAL_HOME
export CATALINA_OPTS="$CATALINA_OPTS -Djava.awt.headless=true -Dorg.apache.catalina.connector.CoyoteAdapter.ALLOW_BACKSLASH=true -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true"
 