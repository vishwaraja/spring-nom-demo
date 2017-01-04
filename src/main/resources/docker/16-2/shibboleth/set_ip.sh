#!/bin/bash

 ip=""

if [[ -z "$DOCKER_HOST" && -z "$HOST" ]];then
        ip=`grep 'eportal' /etc/hosts | awk '{print $1}' |head -n 1`
elif [[ ! -z "$DOCKER_HOST" ]];then
        ip=${DOCKER_HOST//tcp:\/\/}
        ip=${ip//http:\/\/}
        ip=${ip%:*}
elif [[ ! -z "$HOST" ]];then
        ip=${HOST//tcp:\/\/}
        ip=${ip//http:\/\/}
        ip=${ip%:*}
fi

sed -i "s/dockerIp/$ip/g" /opt/shibboleth-idp/metadata/spring_saml_metadata.xml
sed -i "s/idp.shibboleth.com:/$ip:/g" /opt/shibboleth-idp/metadata/idp-metadata.xml
