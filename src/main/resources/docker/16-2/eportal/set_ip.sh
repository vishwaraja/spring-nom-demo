#!/bin/bash

 ip=""

if [[ -z "$DOCKER_HOST" && -z "$HOST" ]];then
        echo "\$DOCKER_HOST and \$HOST is unset or set to the empty string."
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




sed -i "s/http:\/\/nom-eportal./http:\/\/$ip:8444/g" /usr/local/nom/share/eportal-tools/examples/sspi-smb-demo.toml.example
##SSO##sed -i "s/idp.shibboleth.com/$ip/g" /usr/local/nom/etc/eportal/eportal_config.properties
##SSO##sed -i "s/saml.metadata.sp.entity.base.url=/saml.metadata.sp.entity.base.url=http:\/\/$ip:8444\/eportal/g" /usr/local/nom/etc/eportal/eportal_config.properties
