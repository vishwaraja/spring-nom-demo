FROM nominum/cacheserve-centos6_head

COPY setup-cacheserve.sh /usr/local/nom/sbin/setup-cacheserve.sh
COPY add-supervisord-conf.sh /usr/local/nom/sbin/
COPY channel.conf /etc/channel.conf

ENV tell "nom_tell cacheserve"
RUN /usr/local/nom/sbin/add-supervisord-conf.sh

RUN echo 'export TERM=xterm-256color' >> /root/.bashrc
RUN echo 'export PS1="\[$(tput setaf 1)$(tput bold)[\]\\u@\\h \\W]# \[$(tput sgr0)\]"' >> /root/.bashrc
COPY universal-eval.license /usr/local/nom/etc/
