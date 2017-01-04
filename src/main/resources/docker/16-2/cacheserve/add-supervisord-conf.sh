#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-cacheserve]
startsecs=0
command=/usr/local/nom/sbin/setup-cacheserve.sh
EOF
