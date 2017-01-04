#!/bin/bash

policy_manager_ip="localhost";

function wait_for_http() {
  target=$1
  echo waiting for $target to come up
  until curl -s http://$target 2>&1 > /dev/null; do
    echo -n .
    sleep 1
  done
}

function setup_policy_manager() {

   	wait_for_http pm:8080

   	/usr/local/nom/sbin/setup_eportal --ignore-existing -t -f /usr/local/nom/share/eportal-tools/examples/sspi-smb-demo.toml.example -ct 5 -u admin -p admin1 -PM http://pm:8080


}

function curl_policy_manager() {
  method=$1
  data=$2
  path=$3
  curl -i -w "\n" -H "Content-Type: application/json"  -H "Accept: application/json" -u "admin:admin1" --digest -X $method -d "$data" http://pm:8080/${path}
}

setup_policy_manager
