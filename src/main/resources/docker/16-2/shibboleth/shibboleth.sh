#!/bin/sh

TOMCAT_VERSION=8.0
NAME=nom-tomcat-eportal
export CATALINA_HOME="/usr/local/nom/apache-tomcat-$TOMCAT_VERSION"
export CATALINA_BASE="/usr/local/nom/etc/$NAME"
export JRE_HOME="/usr/local/nom/libexec/tomcat-8.0/java"
TOMCAT_SCRIPT="${CATALINA_HOME}/bin/catalina.sh"
#privkey="${CATALINA_BASE}/conf/privkey.pem"
#cert="${CATALINA_BASE}/conf/cert.pem"
#keystore="${CATALINA_BASE}/conf/selfsigned.p12"

prefix="/usr/local/nom/etc/nom-nginx"

#function generate_keys() {
#    if [[ ! -f ${keystore} ]]; then
#        echo Generating self-signed SSL certificate...
#        export RANDFILE=/var/lib/nomssl.rnd
#        /usr/bin/openssl genrsa -out ${privkey} 2048
#        /usr/bin/openssl req -new -x509 -key ${privkey} -out ${cert} -days 1096 -subj /CN=`/bin/hostname`
#        /usr/bin/openssl pkcs12 -export -in ${cert} -inkey ${privkey} -out ${keystore} -name selfsigned -passout pass:selfsigned
#        /bin/chmod 0600 ${keystore}
#        /bin/chown tomcat:tomcat ${keystore}
#        /bin/mv -f ${privkey} ${cert} ${prefix}
#    fi
#}

# Get product specific tomcat options
export USER_MAX_SESSIONS=10
export CATALINA_OPTS="-Xms256m -Xmx512m -server"
export CATALINA_OUT="/var/log/nom-tomcat-eportal/tomcat.out"
export NOM_EPORTAL_HOME=/usr/local/nom/etc/nom-estore
export NOM_ESTORE_LOG_PATH=/var/log/nom-estore

generate_keys
exec ${TOMCAT_SCRIPT} run
