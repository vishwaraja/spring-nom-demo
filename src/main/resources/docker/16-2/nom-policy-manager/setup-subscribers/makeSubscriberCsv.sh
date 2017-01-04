#!/bin/bash
set +x
## Usage:
##   sh makeSubscriberCsv.sh <number of subscribers>
## Put script in: /usr/local/nom/bin

ip2dec () {
    local a b c d ip=$@
    IFS=. read -r a b c d <<< "$ip"
    printf '%d\n' "$((a * 256 ** 3 + b * 256 ** 2 + c * 256 + d))"
}

dec2ip () {
    local ip dec=$@
    for e in {3..0}
    do
        ((octet = dec / (256 ** e) ))
        ((dec -= octet * 256 ** e))
        ip+=$delim$octet
        delim=.
    done
    printf '%s\n' "$ip"
}
next_ip () {
local addr_int base_int next_addr current_addr=$@
addr_int=$(ip2dec $current_addr)
base_int=$(($addr_int + 1))
next_addr=$(dec2ip $base_int)
printf '%s\n' "$next_addr"
}

start_ip="0.0.0.0"
total_subscriber="10"
subscribername_prefix="sub"
if [ ! -z $1 ]; then 
	total_subscriber="$1"; 
fi
if [ ! -z $2 ]; then
	subscribername_prefix="$2";
fi

SCRIPTDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
filename_subscribers="${SCRIPTDIR}/auto_${subscribername_prefix}${total_subscriber}_subscribers.csv"
filename_recipients="${SCRIPTDIR}/auto_${subscribername_prefix}${total_subscriber}_recipients.csv"

>$filename_subscribers
>$filename_recipients
ip=$start_ip
for i in $(seq 1 $total_subscriber); do 
	subscribername=$subscribername_prefix$i;
	echo $subscribername>>$filename_recipients
	ip=$(next_ip $ip)
	printf '%s,%s,,\n' "$subscribername" "$ip" >>$filename_subscribers
done
