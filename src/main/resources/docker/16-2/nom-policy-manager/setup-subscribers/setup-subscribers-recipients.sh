#!/bin/bash
set +x
## Usage:
##   sh setup-subscribers-recipients.sh <number of subscribers>
## Put script in: /usr/local/nom/bin 
## same location as makeSubcriberCsv.sh upload_subscribers upload_recipients

echo 
echo === Executing $0 $@ ===

SCRIPTDIR=/usr/local/nom/bin
#SCRIPTDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
#temporary fix until 'which curl' issue
sed -i 's/exit 1/#exit 1/' $SCRIPTDIR/upload_subscribers
sed -i 's/exit 1/#exit 1/' $SCRIPTDIR/upload_recipients

# make sure policy manager responds
(( c = 40 ))
echo "checking policy manager..."
until curl http://127.0.0.1:8080/status &> /dev/null
do
  (( c-- ))
  if [ $c = 0 ]; then
    echo "error: policy manager is down"
    exit 1
  fi


  echo "checking policy manager..."
  sleep 5
done

total_subscriber="10" #default value
subscribername_prefix="sub" #default string
if [ ! -z $1 ]; then
  total_subscriber=$1
fi
if [ ! -z $2 ]; then
	subscribername_prefix="$2";
fi
filename_subscribers="${SCRIPTDIR}/auto_${subscribername_prefix}${total_subscriber}_subscribers.csv"
filename_recipients="${SCRIPTDIR}/auto_${subscribername_prefix}${total_subscriber}_recipients.csv"
if [ -f "${filename_subscribers}" ]; then
	echo "Skipping makeSubscriberCsv.sh because csv exists already."
else
	sectionname="makeSubscriberCsv"
	starttime=$(date +"%s")
	echo $(date) - Starting $sectioname...
	sh "$SCRIPTDIR/makeSubscriberCsv.sh" ${total_subscriber} ${subscribername_prefix}
	endtime=$(date +"%s")
	echo $(date) - Done $sectionname
	diff=$(($endtime-$starttime))
	echo "Elapsed $sectionname: $(($diff / 60))m $(($diff % 60))s."
fi

sectionname="upload_subscribers"
starttime=$(date +"%s")
echo $(date) - Starting $sectioname...
sh "$SCRIPTDIR/upload_subscribers" "${filename_subscribers}"
endtime=$(date +"%s")
echo $(date) - Done $sectionname
diff=$(($endtime-$starttime))
echo "Elapsed $sectionname: $(($diff / 60))m $(($diff % 60))s."


echo Acquiring recipient-list id: POST /recipient-list
response=`curl -sS -w '\n' -H 'Content-Type: application/json' -H 'Accept: application/json' -u admin:admin1 --digest -X POST -d '{"name": "'"auto_${subscribername_prefix}${total_subscriber}"'"}' http://127.0.0.1:8080/recipient-list`
echo $response | grep error && echo Unable to complete POST /recipient-list && exit 1;

id=`echo $response| grep -Po '(?<=("id":)).*(?=,)'` 
echo Using /recipient-list/${id}
sectionname="upload_recipients"
starttime=$(date +"%s")
echo $(date) - Starting $sectioname...
sh "$SCRIPTDIR/upload_recipients" "$id" "${filename_recipients}"
endtime=$(date +"%s")
echo $(date) - Done $sectionname
diff=$(($endtime-$starttime))
echo "Elapsed $sectionname: $(($diff / 60))m $(($diff % 60))s."
