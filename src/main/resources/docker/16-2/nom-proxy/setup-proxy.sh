#!/bin/sh

set -x

echo "waiting for proxy to answer CC"
until /usr/local/nom/sbin/nom_tell nom-proxy version; do
    sleep .2
done

PROV_PORT=${PROVISIONER_PORT_16002_TCP_PORT}
PROV_ADDR=${PROVISIONER_PORT_16002_TCP_ADDR}
/usr/local/nom/sbin/nom_tell nom-proxy <<EOF
  layer.add name=pm priority=1 server=(${PROV_ADDR} ${PROV_PORT} "secret")
  monitoring.update enable=true kafka={brokers=(nom-kafka:9092)} include=(policy-actions policy-conditions http-request-headers http-response-headers http-user-agent)
  telemetry.update enable=true kafka={brokers=(nom-kafka:9092)}
EOF

