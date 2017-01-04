FROM nominum/aggregator-centos6_16.1.branch

COPY channel.conf /etc/channel.conf
COPY setup-aggregator.sh /usr/local/nom/sbin/setup-aggregator.sh
COPY add-supervisord-conf.sh /usr/local/nom/sbin/
RUN /usr/local/nom/sbin/add-supervisord-conf.sh

RUN echo 'export TERM=xterm-256color' >> /root/.bashrc
RUN echo 'export PS1="\[$(tput setaf 1)$(tput bold)[\]\\u@\\h \\W]# \[$(tput sgr0)\]"' >> /root/.bashrc
COPY universal-eval.license /usr/local/nom/etc/
