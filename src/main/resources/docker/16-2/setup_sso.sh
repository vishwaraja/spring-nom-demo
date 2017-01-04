#!/bin/bash
PREFIX_SSO='##SSO##'
PREFIX_LDAP='##LDAP##'

echo Preparing... docker-compose.yml
sed -i "" "s/$PREFIX_LDAP//g" ./docker-compose.yml
sed -i "" "s/$PREFIX_SSO//g" ./docker-compose.yml

echo Preparing... Dockerfile
sed -i "" "s/$PREFIX_SSO//g" ./eportal/Dockerfile
echo Preparing... set_ip.sh
sed -i "" "s/$PREFIX_SSO//g" ./eportal/set_ip.sh
echo Preparing... supervisord.conf
sed -i "" "s/$PREFIX_SSO//g" ./eportal/supervisord.conf
echo Done...
