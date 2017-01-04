FROM nominum/campaign-authenticity-api-centos6_head:latest

RUN /bin/echo "{http-listen=0.0.0.0#80}" | /usr/local/nom/sbin/campaign-authenticity-api-loadconf -c /var/nom/campaign-authenticity-api/campaign-authenticity-api -t server
