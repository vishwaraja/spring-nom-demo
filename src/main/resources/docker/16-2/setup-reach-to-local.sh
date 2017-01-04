#!/bin/sh

LOCAL_IP=""

networks=("en0" "en1" "en2" "en3" "en4")
for network in "${networks[@]}"
do
        LOCAL_IP=`ifconfig "$network" | awk '{ print $2}' | grep -E -o "([0-9]{1,3}[\.]){3}[0-9]{1,3}"`
	if [ "$LOCAL_IP" ]
	then
		break;
	fi
done

if [ -z "$LOCAL_IP" ]
then
	echo "unable to get host ip address. exiting..."
	exit
fi

# TODO: query /sysinfo on local policy manager to determine whether to continue running

(cat <<EOF
POST /subscriber [{"id": "me", "addresses": ["192.168.59.3"]}]

POST /list {"name": "safe-to-message-domains", "attribution": "nominum"}

POST /list/safe-to-message-domains/nodes {"add": [{"name": "cnn.com"}, {"name": "bbc.com"}, {"name": "xkcd.com"}, {"name": "slashdot.org"}, {"name": "ycombinator.com"}]}

POST /list-group {"name": "safe_to_message", "lists": ["safe-to-message-domains"]}

POST /list-group {"name": "_messaging", "list-groups": ["safe_to_message"]}

POST /list-group {"name": "_loc_messaging", "list-groups": ["_messaging"]}
EOF
) | docker run -i docker.nominum.com/nom-http -s $LOCAL_IP:8080 -u admin -p admin1

docker exec -it compose_proxy_1 bash -c "grep $LOCAL_IP /etc/channel.conf" &> /dev/null
if [ "$?" == "0" ]
then
	echo "command-channel is set on proxy"
else
	docker exec -it compose_proxy_1 bash -c "cp /etc/channel.conf /etc/channel.conf.bu"
	docker exec -it compose_proxy_1 bash -c "sed s/0.0.0.0#16002/$LOCAL_IP#16002/g /etc/channel.conf > /etc/channel.conf.new; cp /etc/channel.conf.new /etc/channel.conf; cat /etc/channel.conf"
fi


docker exec -it compose_cacheserve_1 bash -c "grep $LOCAL_IP /etc/channel.conf" &> /dev/null
if [ "$?" == "0" ]
then
	echo "command-channel is set on cacheserve"
else
	docker exec -it compose_cacheserve_1 bash -c "cp /etc/channel.conf /etc/channel.conf.bu"
	docker exec -it compose_cacheserve_1 bash -c "sed s/0.0.0.0#16002/$LOCAL_IP#16002/g /etc/channel.conf > /etc/channel.conf.new; cp /etc/channel.conf.new /etc/channel.conf; cat /etc/channel.conf"
fi

docker exec -it compose_proxy_1 bash -c "/usr/local/nom/sbin/nom-tell nom-proxy layer.update name=pm \"server=($LOCAL_IP 16002 *)\""
docker exec -it compose_proxy_1 bash -c "/usr/local/nom/sbin/nom-tell nom-proxy layer.reimage name=pm"
sleep 2
docker exec -it compose_proxy_1 bash -c "/usr/local/nom/sbin/nom-tell nom-proxy restart"

docker exec -it compose_cacheserve_1 bash -c "/usr/local/nom/sbin/nom-tell cacheserve layer.update name=pm \"server=($LOCAL_IP 16002 *)\""
docker exec -it compose_cacheserve_1 bash -c "/usr/local/nom/sbin/nom-tell cacheserve layer.reimage name=pm"
sleep 2
docker exec -it compose_cacheserve_1 bash -c "/usr/local/nom/sbin/nom-tell cacheserve restart"
