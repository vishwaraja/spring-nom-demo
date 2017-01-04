#!/bin/sh

cat <<EOF >> /etc/supervisord.conf

[program:set_ip.sh]
startsecs=0
command=/usr/local/nom/sbin/set_ip.sh

[program:generate_keys]
startsecs=0
command=/usr/local/nom/sbin/generate_keys.sh

[program:nom-nginx]
startsecs=0
command=/usr/local/nom/sbin/nginx -c /usr/local/nom/etc/nom-nginx/nginx.conf

[program:run_custom_templates.sh]
startsecs=0
command=/usr/local/nom/sbin/run_custom_templates.sh

EOF
