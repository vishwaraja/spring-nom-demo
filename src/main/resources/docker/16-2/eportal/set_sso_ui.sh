#!/bin/bash
sleep 30
sed -i "s/\"sso\":false/\"sso\":true/g" /usr/local/nom/etc/nom-tomcat-eportal/webapps/ROOT/appConfig.json
