#!/bin/sh

cat <<EOF >> /etc/supervisord.conf
[program:wait-for-postgres]
startsecs=0
command=/usr/local/nom/sbin/wait-for-postgres.sh
priority=1
EOF
