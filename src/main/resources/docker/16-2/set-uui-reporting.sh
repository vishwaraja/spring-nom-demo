#!/usr/bin/env sh

if [ "$1" == "--enabled" ]; then
  CMD="enable=true standby=false"
else
  if [ "$1" == "--disabled" ]; then
    CMD="enable=false standby=false"
  else
    echo "set-uui-reporting.sh [--enabled] [--disabled]"
    echo "configure whether the docker apps portal should run aggregations"
    exit 2
  fi
fi

echo "[log] replacing reporting.standby to match behavior: $1"
docker exec -i compose_uui_1 nom-tell apps-portal reporting.update $CMD
echo "[log] apps portal restarting, you may CTRL-C once complete"
echo
