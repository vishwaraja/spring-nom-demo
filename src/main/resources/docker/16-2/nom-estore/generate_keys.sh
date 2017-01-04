#!/bin/sh

prefix="/usr/local/nom/"
nginx="${prefix}/sbin/nginx"
privkey="${prefix}/etc/nom-nginx/privkey.pem"
cert="${prefix}/etc/nom-nginx/cert.pem"

generate_keys() {
    if [[ ! -f ${privkey} || ! -f ${cert} ]]; then
        echo Generating self-signed SSL certificate...
        export RANDFILE=/var/lib/nomssl.rnd
        /usr/bin/openssl genrsa -out ${privkey} 2048
        /usr/bin/openssl req -new -x509 -key ${privkey} -out ${cert} -days 1096 -subj /CN=`/bin/hostname`
        /bin/chmod 0600 ${privkey}
        /bin/chmod 0600 ${cert}
    fi
}
generate_keys
