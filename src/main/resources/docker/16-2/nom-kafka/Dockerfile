FROM nominum/nom-kafka-centos6_16.2.branch

COPY channel.conf /etc/channel.conf
COPY edit_settings.sh /usr/local/nom/sbin/
COPY add-supervisord-conf.sh /usr/local/nom/sbin/
RUN sed -i "s/advertised.host.name=nomkafka/advertised.host.name=nom-kafka/g" /usr/local/nom/etc/nom-kafka/server.properties
RUN sed -i "s/auto.create.topics.enable=true/auto.create.topics.enable=false/g" /usr/local/nom/etc/nom-kafka/server.properties
RUN sed -i "s/nomkafka/nom-kafka/g" /usr/local/nom/sbin/start-kafka.sh
RUN /usr/local/nom/sbin/add-supervisord-conf.sh
COPY universal-eval.license /usr/local/nom/etc/
