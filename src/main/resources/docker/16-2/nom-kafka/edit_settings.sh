#!/usr/bin/env bash

set -x

address=$2
port=$3
kafka_config=/usr/local/nom/etc/nom-kafka/server.properties

sed -r -i "s/#(advertised.host.name)=(.*)/\1=$address/g" $kafka_config
sed -r -i "s/#(advertised.port)=(.*)/\1=$port/g" $kafka_config

# Restart kafka by killing it and letting supervisord start it.
pkill -f nom-kafka

set +x

