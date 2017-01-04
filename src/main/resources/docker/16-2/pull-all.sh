#!/bin/bash

docker_config=~/.docker/config.json

if [ ! -f $docker_config ];
then
  echo "Please log into the registry with 'docker login docker.nominum.com' and try again."
  exit 1;
fi

# master branch
products=( \
    nom-vertica/centos7_16.2.branch \
    nom-policy-manager/centos7_16.2.branch \
    nompgsql-policy/centos7_16.2.branch \
    nom-provisioning-server/centos7_16.2.branch \
    data-loader/centos7_16.2.branch \
    uui/centos6_16.2.branch \
    nompgsql-appsportal/centos6_16.2.branch \
    condenser/centos6_16.2.branch \
    nom-proxy/centos6_16.2.branch \
    nom-kafka/centos6_16.2.branch  \
    cacheserve/centos6_head \
    nom-estore/centos6_head \
    eportal/centos6_head \
    nom-ssm/centos6_16.2.branch	\
    iptracker/centos6_head	\
    campaign-authenticity-api/centos6_head \
    aggregator/centos6_16.1.branch \
)

for product in ${products[*]}; do
    docker pull "docker.nominum.com/${product}"
done
