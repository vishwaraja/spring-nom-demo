FROM docker.nominum.com/eportal/centos6_head
COPY freeradius.sh /usr/local/freeradius.sh
COPY supervisord.conf /etc/
COPY add_users.py /usr/local/nom/sbin/
# RADIUS
RUN yum install freeradius -y
RUN yum install freeradius-utils -y

ADD ./src/dictionary.nominum /usr/share/freeradius/dictionary.nominum
RUN chmod 444 /usr/share/freeradius/dictionary.nominum
RUN bash -c 'printf "\n\$INCLUDE dictionary.nominum\n" >> /usr/share/freeradius/dictionary'
ADD ./src/default /etc/raddb/sites-available/default
ADD ./src/clients.conf /etc/raddb/clients.conf
ADD ./src/users /etc/raddb/users

EXPOSE 1812/udp
EXPOSE 1813/udp
EXPOSE 1814/udp
EXPOSE 18120/udp
