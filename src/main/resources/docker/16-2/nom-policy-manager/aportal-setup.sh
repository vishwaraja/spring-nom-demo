#!/usr/bin/env bash

curl -i -w "\n" -H "Content-Type: application/json" -u "admin:admin1" --digest -X POST -d '{"name":"all", "servers":[{"url": "http://platform-perf-list-2.eng-lab.nominum.com", "username": "all", "password": "all"}]}' "http://pm:8080/admin/list-service"
