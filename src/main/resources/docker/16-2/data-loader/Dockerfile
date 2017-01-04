FROM nominum/data-loader-centos7_16.2.branch

COPY logback.groovy /usr/local/nom/etc/data-loader/
COPY channel.conf /etc/channel.conf
COPY setup-data-loader.sh /usr/local/nom/sbin/
COPY add-supervisord-conf.sh /usr/local/nom/sbin/

RUN /usr/local/nom/sbin/add-supervisord-conf.sh

RUN echo 'export TERM=xterm-256color' >> /root/.bashrc
RUN echo 'export PS1="\[$(tput setaf 1)$(tput bold)[\]\\u@\\h \\W]# \[$(tput sgr0)\]"' >> /root/.bashrc
COPY universal-eval.license /usr/local/nom/etc/
