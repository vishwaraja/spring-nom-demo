FROM nominum/nom-vertica-centos7_16.2.branch

COPY createTables.sql /usr/local/nom/share/vertica/createTables.sql
COPY createTelemetryTable.sql /usr/local/nom/share/vertica/createTelemetryTable.sql
COPY proxy_engage.csv /usr/local/nom/share/vertica/proxy_engage.csv
COPY dns_engage.csv /usr/local/nom/share/vertica/dns_engage.csv
COPY add-supervisord-conf.sh /usr/local/nom/sbin/add-supervisord-conf.sh
COPY setup-resource-pool.sh /usr/local/nom/sbin/setup-resource-pool.sh
ENV vsqldba "/opt/vertica/bin/vsql -U nomdb -w redwoodcity"
COPY channel.conf /etc/channel.conf

RUN /usr/local/nom/sbin/add-supervisord-conf.sh

RUN echo 'export TERM=xterm-256color' >> /root/.bashrc
RUN echo 'export PS1="\[$(tput setaf 1)$(tput bold)[\]\\u@\\h \\W]# \[$(tput sgr0)\]"' >> /root/.bashrc
COPY universal-eval.license /usr/local/nom/etc/
