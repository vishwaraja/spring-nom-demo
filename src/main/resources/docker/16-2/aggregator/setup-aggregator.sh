#!/bin/sh

set -x

echo "waiting for aggregator to answer CC"
until /usr/local/nom/sbin/nom_tell aggregator version; do
    sleep 1
done

/usr/local/nom/sbin/nom_tell aggregator <<EOF
monitoring.update source=base querystore={max-size=100M}
monitoring.update source=mdr querystore={max-size=100M}
monitoring.update source=lvp querystore={max-size=100M}
EOF
