#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-proxy]
startsecs=0
command=/usr/local/nom/sbin/setup-proxy.sh
EOF
