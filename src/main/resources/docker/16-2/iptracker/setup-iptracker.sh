#!/bin/sh

set -x

echo "waiting for iptracker to answer CC"
until /usr/local/nom/sbin/nom-tell iptracker version; do
    sleep .2
done

/usr/local/nom/sbin/nom-tell iptracker <<EOF
provisioner.add kafka={topic=nom-iptracker-v0} engine-type=iptracker-agent name=iptracker
server.update http-listen=0.0.0.0#8082 http-auth=none
EOF
