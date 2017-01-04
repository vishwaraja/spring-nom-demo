#!/bin/sh

chkconfig slapd on
slapd
ldapadd -f /etc/openldap/slapd.d/cn\=config/tmp/users.ldif -D cn=admin,dc=nominum,dc=com -w admin1
service httpd start
chkconfig httpd on