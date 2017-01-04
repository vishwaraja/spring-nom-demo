#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-iptracker]
startsecs=0
command=/usr/local/nom/sbin/setup-iptracker.sh
EOF
