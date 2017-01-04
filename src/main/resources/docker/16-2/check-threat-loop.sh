#! /bin/bash

while true; do

./set-attribute-for-threat.sh 1051 ta.infected.zeus
./set-attribute-for-threat.sh 1050 ta.infected.conficker

echo " Sleeping for 5, will wake up later and check again "
sleep 5
done

