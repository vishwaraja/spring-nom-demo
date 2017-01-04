#!/bin/bash

set -e

cmd="supervisorctl restart condenser"
until /usr/local/nom/pgsql-9.2/bin/psql -h appspg -p 15433 -U "postgres" -c '\l'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

>&2 echo "Postgres is up - starting condenser"
exec $cmd
