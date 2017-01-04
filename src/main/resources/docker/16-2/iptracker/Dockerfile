FROM nominum/iptracker-centos6_head:latest

COPY setup-iptracker.sh /usr/local/nom/sbin/
COPY add-supervisord-conf.sh /usr/local/nom/sbin/

RUN /usr/local/nom/sbin/add-supervisord-conf.sh
COPY universal-eval.license /usr/local/nom/etc/
