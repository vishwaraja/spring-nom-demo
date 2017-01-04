FROM nominum/eportal-centos6_head
COPY eportal_test_users.properties /usr/local/nom/etc/eportal/
COPY supervisord.conf /etc/
COPY setup-prerequisites.sh /usr/local/nom/sbin/setup-prerequisites.sh
COPY set_ip.sh /usr/local/nom/sbin/set_ip.sh
COPY channel.conf /etc/channel.conf
COPY custom-auth.jar /usr/local/nom/custom-auth.jar

COPY router_devices.json /usr/local/nom/etc/eportal/ROOT/cgi-bin/ndp.lua
COPY router_devices.json /usr/local/nom/etc/eportal/small-business/cgi-bin/ndp.lua

#Setting eportal to run as HTTP (vs HTTPS)
RUN sed -i "s/pi.protocol=https/pi.protocol=http/g" /usr/local/nom/etc/eportal/eportal_config.properties
RUN sed -i "s/nom-policy-manager./pm/g" /usr/local/nom/etc/eportal/eportal_config.properties
RUN sed -i "s/#device-discovery-enabled = .*/device-discovery-enabled = \"true\"/g" /usr/local/nom/share/eportal-tools/examples/sspi-smb-demo.toml.example

RUN sed -i "s/ssm\.hostname=nom-ssm\./ssm\.hostname=ssm/g" /usr/local/nom/etc/eportal/eportal_config.properties

#RADIUS
#RUN sed -i "s/=TEST/=RADIUS/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/=2\.2\.2\.2/=freeradius/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/encryption=false/encryption=true/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/radius\.secret=password/radius\.secret=supersecret/g" /usr/local/nom/etc/eportal/eportal_config.properties

#LDAP
#RUN sed -i "s/=TEST/=LDAP/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/domain\.com/openldap/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/encryption=false/encryption=true/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/OU=UsersX\\\,DC=X\\\,DC=XX\\\,DC=com/OU=Users\\\,DC=nominum\\\,DC=com/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/ldap\.dn\.login=user/ldap\.dn\.login=cn=AdminName Surname\\\,ou=users\\\,dc=nominum\\\,dc=com/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/ldap\.dn\.password=password/ldap\.dn\.password=admin/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/ldap\.dn\.name=mailNickname/ldap\.dn\.name=uid/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/ldap\.role\.property=extensionAttribute2/ldap\.role\.property=/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/ldap\.id\.subscriberId={propertyId}/ldap\.id\.subscriberId={uid}/g" /usr/local/nom/etc/eportal/eportal_config.properties
#RUN sed -i "s/ldap\.access\.type\.property=propertyType/ldap\.access\.type\.property=employeeType/g" /usr/local/nom/etc/eportal/eportal_config.properties

#SHIBBOLETH
##SSO##COPY set_sso_ui.sh /usr/local/nom/sbin/set_sso_ui.sh
##SSO##RUN sed -i "s/=TEST/=SAML/g" /usr/local/nom/etc/eportal/eportal_config.properties
##SSO##RUN sed -i "s/saml\.metadata\.idp\.url=https:\/\/idp\.ssocircle\.com\/idp-meta\.xml/saml\.metadata\.idp\.url=http:\/\/idp\.shibboleth\.com:8448\/idp\/shibboleth /g" /usr/local/nom/etc/eportal/eportal_config.properties
##SSO##RUN sed -i "s/saml\.role\.property=propertyRole/saml\.role\.property=businessCategory/g" /usr/local/nom/etc/eportal/eportal_config.properties
##SSO##RUN sed -i "s/saml\.name\.property=propertyName/saml\.name\.property=uid/g" /usr/local/nom/etc/eportal/eportal_config.properties
##SSO##RUN sed -i "s/saml\.id\.subscriberId={propertyId}/saml\.id\.subscriberId={uid}/g" /usr/local/nom/etc/eportal/eportal_config.properties
##SSO##RUN sed -i "s/saml\.access\.type\.property=propertyAccessType/saml\.access\.type\.property=employeeType/g" /usr/local/nom/etc/eportal/eportal_config.properties

#CUSTOM
#RUN sed -i "s/=TEST/=CUSTOM/g" /usr/local/nom/etc/eportal/eportal_config.properties
COPY universal-eval.license /usr/local/nom/etc/
