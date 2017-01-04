#!/bin/sh

echo
echo "checking cacheserve..."
dig @`docker-machine ip default` nominum.com &> /dev/null
if [ "$?" != "0" ]; then
  echo "error: cacheserve is down"
  docker exec -it compose_cacheserve_1 bash -c "egrep -ir 'error|critical|exception' /var/log/ | sort | uniq"
fi

echo
echo "checking nom-proxy..."
curl http://`docker-machine ip default`/ --header "Host: nominum.com" &> /dev/null
if [ "$?" != "0" ]; then
  echo "error: nom-proxy is down"
  docker exec -it compose_proxy_1 bash -c "egrep -ir 'error|critical|exception' /var/log/ | sort | uniq"
fi


# make sure policy manager responds
(( c = 2 ))
echo
echo "checking policy manager..."
until curl http://`docker-machine ip default`:8080/status &> /dev/null
do
  (( c-- ))
  if [ $c = 0 ]; then
    echo "error: policy manager is down"
    docker exec -it compose_pm_1 bash -c "egrep -ir 'error|critical|exception' /var/log/ | sort | uniq"
    break
  fi

  echo "checking policy manager..."
  sleep 2
done


(( c = 2 ))
echo
echo "checking apps portal..."
until curl http://`docker-machine ip default`:8081/ &> /dev/null
do
  (( c-- ))
  if [ $c = 0 ]; then
    echo "error: apps portal is down"
    docker exec -it compose_uui_1 bash -c "egrep -ir 'error|critical|exception' /var/log/ | sort | uniq"
    break
  fi

  echo "checking apps portal..."
  sleep 3
done

echo
echo "checking vertica..."
docker exec -it compose_vertica_1 bash -c '/opt/vertica/bin/vsql -U nomdb -w redwoodcity n2 -c "\d nomc.events;"'  &> /dev/null
if [ "$?" != "0" ]; then
  docker exec -it  compose_vertica_1 bash -c "fgrep ERROR /var/nom/nomdb/catalog/n2/v_n2_node0001_catalog/vertica.log | tail"
fi

echo
echo "checking kafka..."
docker exec -it compose_nom-kafka_1 bash -c 'nom-kafka-metadata --brokers localhost:9092 | grep nom-proxy-transaction' > /dev/null
if [ "$?" != "0" ]; then
  echo "error: kafka is down"
  docker exec -it compose_nom-kafka_1 bash -c "egrep -ir 'error|critical|exception' /var/log/ | sort | uniq | head -5"
fi

echo
echo "checking data-loader..."
DLUP=`docker exec -it compose_loader_1 bash -c '/usr/local/nom/sbin/nom_tell nom-data-loader version'`
if [[ $DLUP != *"Data Loader"* ]]
then
  echo "error: data-loader is down"
  docker exec -it compose_loader_1 bash -c "egrep -ir 'error|critical|exception' /var/log/ | sort | uniq"
fi

echo "done."
