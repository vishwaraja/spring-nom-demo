#!/bin/sh

stack_name=""

pm_container_name=$(docker ps 2>/dev/null | grep "${stack_name}_pm " | awk '{print $(NF)}' | head -1)
vertica_container_name=$(docker ps 2>/dev/null | grep "${stack_name}_vertica " | awk '{print $(NF)}' | head -1)

# update request conditions
vsql_request="select distinct view from nom.dns where start_time > CURRENT_TIMESTAMP - INTERVAL '15 SECOND' and threat_id = ${1};"


    data=($(docker exec -it ${vertica_container_name} /opt/vertica/bin/vsql -U nomdb n2 -w redwoodcity -t -q -A -c "${vsql_request}"))

    for sub in "${data[@]}"
    do
        value="${1}"
        request_body="{\"add\":[{\"subscriber\":\"$(echo ${sub} | tr -d '\r\n')\",\"name\":\"${2}\",\"value\":\"${value}\"}]}"
        echo "POST /subscriber-attribute ${request_body}"
        docker exec -i $pm_container_name curl -sqk -w "\n" -H "Content-Type: application/json" -H "Accept: application/json" -u "admin:admin1" -X POST -d "${request_body}" --digest http://localhost:8080/subscriber-attribute
    done


