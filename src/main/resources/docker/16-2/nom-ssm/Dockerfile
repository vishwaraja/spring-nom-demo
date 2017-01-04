FROM nominum/nom-ssm-centos6_16.2.branch

# Set the locale
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

COPY channel.conf /etc/channel.conf

COPY set_ip.sh /usr/local/nom/sbin/set_ip.sh
COPY add-supervisord.conf /usr/local/nom/sbin/add-supervisord.conf
RUN /usr/local/nom/sbin/add-supervisord.conf
RUN mv /usr/local/nom/etc/nom-ssm/reports/disabled/engage/device-discovery.groovy /usr/local/nom/etc/nom-ssm/reports/enabled/engage

# nom-ssm engine configuration
## disables server security
## enables CORS
## enables Subscriber Creation
## enables TeeFilter on requests for logback-access.xml
RUN /bin/echo "{http-auth=none cross-origin-requests={enable=true} enable-subscriber-management=true access-log-contents=true http-rate-limiter={enable=false}}" | /usr/local/nom/sbin/nom-ssm-loadconf -c /var/nom/nom-ssm/nom-ssm -t server

## points nom-ssm to the linked container aliased as pm
RUN /bin/echo "{hosts=(pm#8080)}" | /usr/local/nom/sbin/nom-ssm-loadconf -c /var/nom/nom-ssm/nom-ssm -t policy-manager

## points nom-ssm to the linked container aliased as vertica
RUN /bin/echo "{host=vertica#5433}" | /usr/local/nom/sbin/nom-ssm-loadconf -c /var/nom/nom-ssm/nom-ssm -t vertica

## enables reporting in aggregate mode
RUN /bin/echo "{enable=true standby=false}" | /usr/local/nom/sbin/nom-ssm-loadconf -c /var/nom/nom-ssm/nom-ssm -t reporting

# nom-ssm config properties
RUN sed -i "s/#engage.pi.device-groups=.*/engage.pi.device-groups=pi-dg1,pi-dg2,pi-dg3/g" /usr/local/nom/etc/nom-ssm/config.properties
RUN sed -i "s/#engage.sb.device-groups=.*/engage.sb.device-groups=pi-dg1,pi-dg2,pi-dg3,pi-dg4,pi-dg5,pi-dg6,pi-dg7/g" /usr/local/nom/etc/nom-ssm/config.properties
RUN sed -i "s/name=\"com.nominum\" level=\"INFO\"/name=\"com.nominum\" level=\"DEBUG\"/g" /usr/local/nom/etc/nom-ssm/logback-spring.xml

RUN sed -i "s/device-list=.*$/device-list=\/cgi-bin\/ndp\.lua/g" /usr/local/nom/etc/nom-ssm/config.properties
COPY universal-eval.license /usr/local/nom/etc/
