#!/bin/bash
function wait_for_http() {
  target=$1
  echo waiting for $target to come up
  until curl -s http://$target 2>&1 > /dev/null; do
    echo -n .
    sleep 1
  done
}

function curl_uui() {
  method=$1
  data=$2
  path=$3
  #curl -i -w "\n" -H "Content-Type: application/json"  -H "Accept: application/json" -u "admin:admin1" --digest -X $method -d "$data" http://$component_ip:$component_port/${path}
  curl -i -w "\n" -H "Content-Type: application/json"  -H "Accept: application/json" -X $method -d "$data" http://localhost:8081/${path}
}

function setup_apps_portal() {
  echo Setting up apps portal
  wait_for_http localhost:8081

  curl_uui POST '{"name": "docker_agg","secret":"secret","hostname":"agg","port":"9995","wait":true,"portalUsername":"admin","portalPassword":"admin1"}' api/1/aggregator.json
  curl_uui POST '{"name":"docker_pm","hostname":"pm","port":"8080","username":"admin","password":"admin1","ssl":"false","enabled":true,"wait":true,"portalUsername":"admin","portalPassword":"admin1"}' api/1/policyManager.json

  wait_for_http pm:8080

  curl -w '\n' -H 'Content-Type: application/json' -H 'Accept: application/json' -c /tmp/cookie.txt --digest -X POST -d '{"email":"admin","password":"admin1"}' http://localhost:8081/auth
  curl -w '\n' -H 'Content-Type: application/json' -H 'Accept: application/json' -b /tmp/cookie.txt --digest -X GET http://localhost:8081/component/policyManager/connect/1
}

setup_apps_portal
