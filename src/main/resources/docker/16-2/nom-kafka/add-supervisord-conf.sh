cat <<EOF >> /etc/supervisord.conf

[program:configure-topics]
startsecs=0
command=/usr/local/nom/sbin/nom-kafka-configure-topics  --disk-size 2
EOF
