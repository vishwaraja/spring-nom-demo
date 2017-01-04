FROM nominum/nom-proxy-centos6_16.2.branch

COPY setup-proxy.sh /usr/local/nom/sbin/setup-proxy.sh
COPY add-supervisord-conf.sh /usr/local/nom/sbin/
COPY channel.conf /etc/channel.conf

ENV tell "nom_tell nom-proxy"
ENV NOM_OPTIONS "--allow-explicit --allow-local "
RUN sed -i 's#/usr/local/nom/sbin/nom-proxy -f#/usr/local/nom/sbin/nom-proxy -f %(ENV_NOM_OPTIONS)s#g' /etc/supervisord.conf
RUN /usr/local/nom/sbin/add-supervisord-conf.sh
COPY universal-eval.license /usr/local/nom/etc/
