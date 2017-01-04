FROM nominum/uui-centos6_16.2.branch:latest

COPY add-supervisord.conf /usr/local/nom/sbin/add-supervisord.conf
COPY regExOverrides.json /usr/local/nom/etc/regExOverrides.json
COPY setup-prerequisites.sh /usr/local/nom/sbin/
COPY set_ip.sh /usr/local/nom/sbin/set_ip.sh
COPY channel.conf /etc/channel.conf
RUN yum -y install rsyslog
RUN echo "# Provides UDP syslog reception" >> /etc/rsyslog.conf
RUN echo "$ModLoad imudp" >> /etc/rsyslog.conf
RUN echo "$UDPServerRun 514" >> /etc/rsyslog.conf
RUN echo "/etc/init.d/rsyslog start" > /start_aportal.sh
RUN echo "cd /usr/local/nom/sbin" >> /start_aportal.sh
RUN echo "setup_aportal -u admin -port 8080 -f /usr/local/nom/share/aportal-tools/examples/threatavert-setup.toml -p admin1 -s pm" >> /start_aportal.sh
RUN chmod +x /start_aportal.sh
RUN /usr/local/nom/sbin/add-supervisord.conf
RUN sed -i '/apps.N2Engage.enable = true/a apps.TestData.enable = true' /usr/local/nom/etc/uui/application.conf

COPY universal-eval.license /usr/local/nom/etc/
