#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-prerequisites.sh]
startsecs=0
command=/usr/local/nom/sbin/setup-prerequisites.sh
EOF
