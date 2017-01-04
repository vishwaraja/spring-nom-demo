FROM nominum/nom-policy-manager-centos7_16.2.branch

COPY vsql /opt/vertica/bin/vsql
COPY setup-subscribers/* /usr/local/nom/bin/
COPY setup-prerequisites.sh /usr/local/nom/sbin/
COPY add-supervisord-conf.sh /usr/local/nom/sbin/
COPY channel.conf /etc/channel.conf
COPY aportal-setup.sh /aportal_setup.sh

RUN sed -i "s/#iptracker.enabled=false/iptracker.enabled=true/" /usr/local/nom/etc/nom-policy-manager/config.properties
RUN sed -i "s/#iptracker.kafka.cluster.0=host1:9092,host2:9092,host3:9092/iptracker.kafka.cluster.0=nom-kafka:9092/" /usr/local/nom/etc/nom-policy-manager/config.properties
RUN /usr/local/nom/sbin/add-supervisord-conf.sh

RUN echo 'export TERM=xterm-256color' >> /root/.bashrc
RUN echo 'export PS1="\[$(tput setaf 1)$(tput bold)[\]\\u@\\h \\W]# \[$(tput sgr0)\]"' >> /root/.bashrc
RUN chmod +x /aportal_setup.sh

COPY universal-eval.license /usr/local/nom/etc/
