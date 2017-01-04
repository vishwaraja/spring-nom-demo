#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-aggregator]
startsecs=0
command=/usr/local/nom/sbin/setup-aggregator.sh
EOF
