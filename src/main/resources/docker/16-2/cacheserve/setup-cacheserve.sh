#!/bin/sh

set -x

echo "waiting for cacheserve to answer CC"
until /usr/local/nom/sbin/nom_tell cacheserve version; do
    sleep .2
done

PROV_PORT=${PROVISIONER_PORT_16002_TCP_PORT}
PROV_ADDR=${PROVISIONER_PORT_16002_TCP_ADDR}
AGG_ADDR=${AGG_PORT_14252_TCP_ADDR}
PROXY_ADDR=""

if [[ -z "$DOCKER_HOST" && -z "$HOST" ]];then
        echo "\$DOCKER_HOST and \$HOST is unset or set to the empty string. setting preload to ${PROXY_PORT_15003_TCP_ADDR}"
        PROXY_ADDR=${PROXY_PORT_15003_TCP_ADDR}
elif [[ ! -z "$DOCKER_HOST" ]];then
        PROXY_ADDR=${DOCKER_HOST//tcp:\/\/}
        PROXY_ADDR=${PROXY_ADDR//http:\/\/}
        PROXY_ADDR=${PROXY_ADDR%:*}
elif [[ ! -z "$HOST" ]];then
        PROXY_ADDR=${HOST//tcp:\/\/}
        PROXY_ADDR=${PROXY_ADDR//http:\/\/}
        PROXY_ADDR=${PROXY_ADDR%:*}
fi
/usr/local/nom/sbin/nom_tell cacheserve <<EOF
  server.update time-zone=UTC
  resolver.add name=pm-resolver preload=((nom-proxy A ${PROXY_ADDR})) preload-nxrrset=((nom-proxy AAAA))
  layer.add name=pm priority=1 server=(${PROV_ADDR} ${PROV_PORT} "secret")
  monitoring.update querystore={max-size=50M}
  monitoring.update analytics-engine={simple-target=(${AGG_ADDR}#14252 secret) enable=1}
  monitoring.update statmon-kafka={brokers=(nom-kafka:9092)}
  telemetry.update enable=true kafka={brokers=(nom-kafka:9092)}
EOF
