#!/usr/bin/env bash

docker exec compose_pm_1 bash /aportal_setup.sh
docker exec compose_uui_1 bash /start_aportal.sh