#!/bin/bash

wait_for_vertica_version() {
    echo "waiting for Vertica to start"

    until /opt/vertica/bin/vsql -U nomdb -w redwoodcity -c "select version();" > /dev/null
    do
        echo -n .
        sleep 1
    done
}

wait_for_vertica_qcount() {
    echo "waiting for Vertica to create nomr.dns_qcount_by_device table"
    until /opt/vertica/bin/vsql -U nomdb -w redwoodcity -c "select count(*) from nomr.dns_qcount_by_device;" > /dev/null
    do
        echo -n .
        sleep 1
    done
}

change_resource_pool() {
  sleep 5
     /opt/vertica/bin/vsql -U nomdb  -w redwoodcity n2 -c "select do_tm_task('moveout');"
     /opt/vertica/bin/vsql -U nomdb  -w redwoodcity n2 -c "alter RESOURCE POOL WOSDATA memorysize '2G' maxmemorysize '2G';"
}

installFlexTableSupport() {
    /opt/vertica/bin/vsql -v SEARCH_PATH="public" -f /opt/vertica/packages/flextable/ddl/install.sql -w redwoodcity n2 nomdb
}

createTelemetryTable() {
    /opt/vertica/bin/vsql -v SEARCH_PATH="public" -f /usr/local/nom/share/vertica/createTelemetryTable.sql -w redwoodcity n2 nomdb
}

wait_for_vertica_version
installFlexTableSupport
createTelemetryTable

wait_for_vertica_qcount
change_resource_pool
