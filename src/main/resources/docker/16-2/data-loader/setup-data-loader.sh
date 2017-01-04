#!/bin/sh

set -x

echo "waiting for data-loader to answer CC"
until /usr/local/nom/sbin/nom_tell nom-data-loader version; do
    sleep .2
done

/usr/local/nom/sbin/nom_tell nom-data-loader <<EOF
  vertica.update host=vertica
  telemetry.update enable=1
  loader.add name=dns kafka={brokers=(nom-kafka) topic=nom-dns-base} prune-after=7200 vertica-parser='DnsParser()' vertica-table=nom.dns
  loader.add name=proxy kafka={brokers=(nom-kafka) topic=nom-proxy-transaction} prune-after=7200 vertica-parser='ProxyParser()' vertica-table=nom.proxy
  loader.add name=events kafka={brokers=(nom-kafka) topic=nom-proxy-transaction} prune-after=864000 vertica-parser=CampaignParser() vertica-table=nomc.events
EOF
