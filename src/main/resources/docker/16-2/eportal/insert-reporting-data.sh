#!/bin/bash

function usage {
    echo "usage: insert-reporting-data.sh [[-s <docker stack name>] | [-id <subscriber Id>] | [-h]]"
}

stack_name=""
subscriber_id=""
reporting_data="eportal/reporting-data.sql"
subscriber_limit=10

while [ "$1" != "" ]; do
    case $1 in
        -s | --stack )           shift
            stack_name=$1
        ;;
        -id | --subscriber-id )           shift
            subscriber_id=$1
        ;;
        -h | --help )           usage
            exit
        ;;
        * )                     usage
            exit 1
    esac
    shift
done

pm_container_name=$(docker ps 2>/dev/null | grep "${stack_name}_pm " | awk '{print $(NF)}' | head -1)
vertica_container_name=$(docker ps 2>/dev/null | grep "${stack_name}_vertica " | awk '{print $(NF)}' | head -1)
ssm_container_name=$(docker ps 2>/dev/null | grep "${stack_name}_ssm " | awk '{print $(NF)}' | head -1)

if [ -z "$subscriber_id" ]; then
    if ! which jq > /dev/null; then
        echo "jq package is not installed."
        echo "List of available subscriberIds will be not presented."
    else
        subscribers_response=$(docker exec -i $pm_container_name curl -sqk -w "\n" -H "Content-Type: application/json" -H "Accept: application/json" -u "admin:admin1" --digest http://localhost:8080/subscriber?limit=$subscriber_limit)
        subscribers=$(echo $subscribers_response | jq '.content[].id' | sed -e "s/\"//g")
        if [ "$(echo $subscribers_response | jq '.limit')" == $subscriber_limit ]; then
            echo "List of $subscriber_limit random subscriberIds:"
        else
            echo "List of available subscriberIds:"
        fi
        echo "$subscribers"
    fi

    echo
    echo "Enter subscriberId: "
    read subscriber_id
fi
replaceString="s/PROFILE_NAME/$subscriber_id/g"

if [ ! -f $(pwd)/$reporting_data ]; then
    echo "$reporting_data is not found."
    exit 1
fi

echo "Inserting reporting data for subscriber '$subscriber_id' ..."
cd eportal 2> /dev/null
cat reporting-data.sql | sed $replaceString | docker exec -i $vertica_container_name bash -c 'cat > /usr/local/nom/etc/reporting-data.sql'
docker exec -i $vertica_container_name /opt/vertica/bin/vsql -h 127.0.0.1 -U nomdb -f /usr/local/nom/etc/reporting-data.sql -w redwoodcity > /dev/null
echo "Inserting in DB finished successfully"
echo "Running aggregation"
docker exec -i $ssm_container_name nom-tell nom-ssm aggregation.run
echo "Running accelerator sync"
docker exec -i $ssm_container_name nom-tell nom-ssm accelerator.sync-all

echo "Finished successfully"
