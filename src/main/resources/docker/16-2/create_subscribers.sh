#!/bin/bash
if [ -z $DOCKER_HOST ] 
then
  echo "DOCKER_HOST not set.  This script assumes you are using docker-machine from Vish!"
  exit
fi

printf "\n\n\n"
echo "########################################################################################"
echo "########################################################################################"
echo "Please tell us the number of subscribers you would like to create"
echo "You may also tell us the attribute you want to add for that subscriber"
echo "   NOTE: If you do not enter an attribute, the script will only create the subscribers."
echo "########################################################################################"
echo "########################################################################################"
printf "\n\n\n"

DOCKER=`echo $DOCKER_HOST|awk -F: '{print $2}'|sed 's/\///g'`

ARGS=2

if [ $# -lt "$ARGS" ]
then
  echo "How many subscribers do you want to create?  "
  read COUNT
  echo "What subscriber attribute name do you want to create?  "
  echo "   (press [enter/return] if you want to skip) "
  read SUBATTR
else
  while [[ $# > 1 ]]
  do
  key="$1"

  case $key in
    -c|--count)
    COUNT="$2"
    shift # past argument
    ;;
    -a|--attribute)
    SUBATTR="$2"
    ;;
    -z|--autoload)
    AUTOLOAD="$2"
    ;;
    --default)
    DEFAULT=YES
    ;;
    *)
            # unknown option
    ;;
  esac
  shift # past argument or value
  done  
fi

AUTOLOAD="YES"

### Set the loops for creating the IP addresses and subscriber ID's
if [[ $SUBATTR == "" ]]; then
  AUTOLOAD="NO"
fi

let "BIG=$COUNT/256"
let "SMALL=$COUNT%256 - 1"
let "counter=0"

### Here is the location of the Policy Manager
CURLURL="http://$DOCKER:8080/subscriber"
CT="Content-Type: application/json"
USER="admin:admin1"

### Loop through the /24
while [ $BIG -gt 0 ]
do
  ### Loop through the /32
  for j in `seq 0 255`
  do
    SUBtemp='"sub-'$COUNT'-'$((counter+1))'"'
    SUB=`md5 <<< "$SUBtemp $(date)"`
    echo $SUB >> file$COUNT.csv
    CURLDATA='[{"id": "'$SUB'", "addresses":["192.188.'$BIG'.'$j'"]}]'
    RESP=`curl -s -i -w "\n" -H "$CT" -u "$USER" --digest -X POST -d "$CURLDATA" $CURLURL`

    if [[ $RESP == *"partial-success"* ]]; then
      echo $SUB: already loaded
    elif [[ $RESP == *"success"* ]]; then
      printf "."
    else
      echo $SUB: Other failure
      echo $RESP
    fi

    let "counter+=1"
  done
let "BIG-=1"
done

### Loop through the FINAL /32
for j in `seq 0 $SMALL`
do
  SUBtemp='"sub-'$COUNT'-'$((counter+1))'"'
  SUB=`md5 <<< "$SUBtemp $(date)"`
  echo $SUB >> file$COUNT.csv
  CURLDATA='[{"id": "'$SUB'", "addresses":["192.188.0.'$j'"]}]'
  RESP=`curl -s -i -w "\n" -H "$CT" -u "$USER" --digest -X POST -d "$CURLDATA" $CURLURL`

  if [[ $RESP == *"partial-success"* ]]; then
    echo $SUB: already loaded
  elif [[ $RESP == *"success"* ]]; then
    printf "."
  else
    echo $SUB: Other failure
  fi

  let "counter+=1"
done

sed 's/"//g' file$COUNT.csv > final_file_$COUNT.csv
rm file$COUNT.csv

#### Create the JSON for creating the subscriber attributes
STARTER='{"add":[{"subscriber":"'
PRE_CONNECTOR='","name":"'
POST_CONNECTOR='"},{"subscriber":"'
ENDER='"}]}'

CURLURL_SUBATTR="http://$DOCKER:8080/subscriber-attribute"

SUBATTR_PAYLOAD=$STARTER
LINECOUNT=0
for sub in `cat final_file_$COUNT.csv`
do
  LINECOUNT=$((LINECOUNT+1))
  if [[ "$LINECOUNT" -lt "$COUNT" ]]; then
    SUBATTR_PAYLOAD="$SUBATTR_PAYLOAD$sub$PRE_CONNECTOR$SUBATTR$POST_CONNECTOR"
  else
    SUBATTR_PAYLOAD="$SUBATTR_PAYLOAD$sub$PRE_CONNECTOR$SUBATTR$ENDER"
  fi
done

if [ $AUTOLOAD == "YES" ]; then
   echo "...."
   echo "...."
   echo "Autoloading in 2 seconds..."
   sleep 2

   RESP=`curl -s -i -w "\n" -H "$CT" -u "$USER" --digest -X POST -d "$SUBATTR_PAYLOAD" $CURLURL_SUBATTR`

   if [[ $RESP == *"nom-policy"* ]]; then
     echo " "
     echo "Success:  Created $COUNT subscribers with attribute $SUBATTR"
   else 
     echo FAILURE:  Failed to load subscriber-attributes.
     echo $RESP
   fi
else
   echo " "
   echo "Subscriber attributes not loaded."
fi

## Cleaning up subscriber ID file
rm final_file_*