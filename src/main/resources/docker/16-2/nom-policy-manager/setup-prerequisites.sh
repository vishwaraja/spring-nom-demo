#!/bin/bash

function wait_for_http() {
  target=$1
  echo waiting for $target to come up
  until curl -s http://$target 2>&1 > /dev/null; do
    echo -n .
    sleep 1
  done
}


function setup_policy_manager() {

  echo Setting up policy manager

   wait_for_http localhost:8080

  #create subscriber
  echo Setting up 'me' subscriber
  curl_policy_manager POST  '[{"id": "me", "addresses":["192.168.99.0/24","192.168.59.0/24","172.17.42.1"]}]' subscriber
  echo Setting up 'me' recipient_list
  curl_policy_manager POST '{"name": "me"}' recipient-list
  curl_policy_manager POST '["me"]' recipient-list/1/recipients

  #create domain list, add content
  echo Setting up 'test news sites' list node
  curl_policy_manager POST '{"name": "test news sites", "attribution": "nominum"}' list
  curl_policy_manager POST '{"add": [{"name":"cnn.com"},{"name":"bbc.com"},{"name":"mashable.com"},{"name":"boingboing.net"},{"name":"example.com"}]}' list/test%20news%20sites/nodes

  #create list group, add list to list group safe_to_message
  echo Setting up 'safe_to_message' list-group
  curl_policy_manager POST '{"name": "safe_to_message", "attribution": "nominum"}' list-group
  curl_policy_manager PUT '{"lists": ["test news sites"]}' list-group/safe_to_message

  #create _messaging list group and add safe_to_message
  curl_policy_manager POST '{"name": "_messaging", "attribution": "nominum"}' list-group
  curl_policy_manager PUT '{"list-groups": ["safe_to_message"]}' list-group/_messaging

  #create _loc_messaging group and add _messaging
  curl_policy_manager POST '{"name": "_loc_messaging", "attribution": "nominum"}' list-group
  curl_policy_manager PUT '{"list-groups": ["_messaging"]}' list-group/_loc_messaging

  #create engines
  curl_policy_manager POST '{"name": "proxy", "engine-type": "nom-proxy", "command-channel": [{"port": "15003", "secret": "secret"}]}' engine/nom-proxy
  curl_policy_manager POST '{"name": "cacheserve", "engine-type": "cacheserve", "command-channel": [{"port": "9434", "secret": "secret"}]}' engine/cacheserve

}


function curl_policy_manager() {
  method=$1
  data=$2
  path=$3
  curl -i -w "\n" -H "Content-Type: application/json"  -H "Accept: application/json" -u "admin:admin1" --digest -X $method -d "$data" http://localhost:8080/${path}
}
setup_policy_manager
