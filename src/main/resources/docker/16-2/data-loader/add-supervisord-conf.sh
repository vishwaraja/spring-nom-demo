#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:setup-data-loader]
startsecs=0
command=/usr/local/nom/sbin/setup-data-loader.sh
EOF
