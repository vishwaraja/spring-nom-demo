FROM nominum/condenser-centos6_16.2.branch

COPY channel.conf /etc/channel.conf
COPY wait-for-postgres.sh /usr/local/nom/sbin/
RUN sed -i '/setcap/s/^/#/' /usr/local/nom/sbin/condenser
COPY add-supervisord-conf.sh /usr/local/nom/sbin/
RUN /usr/local/nom/sbin/add-supervisord-conf.sh
ENV PATH $PATH:$HOME/bin:/usr/local/nom/libexec/uui/java/bin

RUN echo 'export TERM=xterm-256color' >> /root/.bashrc
RUN echo 'export PS1="\[$(tput setaf 1)$(tput bold)[\]\\u@\\h \\W]# \[$(tput sgr0)\]"' >> /root/.bashrc

COPY universal-eval.license /usr/local/nom/etc/
