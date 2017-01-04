FROM docker.nominum.com/eportal/centos6_head
COPY openldap.sh /usr/local/openldap.sh
COPY supervisord.conf /etc/

# LDAP
RUN yum install -y openldap openldap-clients openldap-servers

ADD ./src/olcDatabase\=\{2\}bdb.ldif /etc/openldap/slapd.d/cn\=config
RUN chmod 666 /etc/openldap/slapd.d/cn\=config/olcDatabase\=\{2\}bdb.ldif
RUN mkdir /etc/openldap/slapd.d/cn\=config/tmp
ADD ./src/users.ldif /etc/openldap/slapd.d/cn\=config/tmp
RUN chmod 666 /etc/openldap/slapd.d/cn\=config/tmp/users.ldif


#PHP LDAP ADMIN (GUI for LDAP)
RUN yum -y install php php-ldap
RUN yum -y install wget
RUN wget http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
RUN wget http://rpms.famillecollet.com/enterprise/remi-release-6.rpm
RUN rpm -Uvh remi-release-6*.rpm epel-release-6*.rpm
RUN yum --enablerepo=epel -y install phpldapadmin
ADD ./src/phpldapadmin.conf /etc/httpd/conf.d/phpldapadmin.conf
RUN chmod 666 /etc/httpd/conf.d/phpldapadmin.conf
ADD ./src/config.php /etc/phpldapadmin/config.php
RUN chmod 666 /etc/phpldapadmin/config.php


