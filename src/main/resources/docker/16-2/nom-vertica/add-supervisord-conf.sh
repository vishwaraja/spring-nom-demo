#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-resource-pool.sh]
startsecs=0
command=/usr/local/nom/sbin/setup-resource-pool.sh
EOF
