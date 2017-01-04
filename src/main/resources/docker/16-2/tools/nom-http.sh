#!/bin/sh
container=`docker ps -a -q --filter=name=compose_ssm`
gateway=`docker inspect --format '{{range .NetworkSettings.Networks}}{{.Gateway}}{{end}}' $container`
if [ "$1" == "pm" ]; then
	docker run -it docker.nominum.com/nom-http/centos6_head -s $gateway:8080 -u admin -p admin1
elif [ "$1" == "ssm" ]; then
	docker run -it docker.nominum.com/nom-http/centos6_head -s $gateway:9090 -u admin -p admin1
elif [ "$1" == "ipt" ]; then
        docker run -it docker.nominum.com/nom-http/centos6_head -s $gateway:8082 -u admin -p admin1
else
	echo "usage: $0 pm | ssm | ipt"
	exit 1
fi
exit 0
