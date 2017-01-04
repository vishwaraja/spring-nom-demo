#!/bin/bash

 ip=""

if [[ -z "$DOCKER_HOST" && -z "$HOST" ]];then
        echo "\$DOCKER_HOST and \$HOST is unset or set to the empty string."
        ip=`grep 'estore' /etc/hosts | awk '{print $1}' |head -n 1`
elif [[ ! -z "$DOCKER_HOST" ]];then
        ip=${DOCKER_HOST//tcp:\/\/}
        ip=${ip//http:\/\/}
        ip=${ip%:*}
elif [[ ! -z "$HOST" ]];then
        ip=${HOST//tcp:\/\/}
        ip=${ip//http:\/\/}
        ip=${ip%:*}
fi

sed -i "s/estore.ctu_url=\"\"/estore.ctu_url=\"http:\/\/$ip:8445\/customTemplates.json\"/g" /usr/local/nom/etc/uui/application.conf
sed -i "s/estore.ews_url=\"\"/estore.ews_url=\"http:\/\/$ip:8445\/ewsDashboard.json\"/g" /usr/local/nom/etc/uui/application.conf
sed -i "s/estore.ar_url=\"\"/estore.ar_url=\"http:\/\/$ip:8445\/advancedReporting.json\"/g" /usr/local/nom/etc/uui/application.conf
sed -i "s/estore.uar_url=\"\"/estore.uar_url=\"http:\/\/$ip:8445\/userAgentRegex.json\"/g" /usr/local/nom/etc/uui/application.conf

