FROM docker.nominum.com/eportal/centos6_head

COPY shibboleth.sh /usr/local/shibboleth.sh
COPY supervisord.conf /etc/supervisord.conf
COPY set_ip.sh /usr/local/nom/sbin/set_ip.sh

#--------JAVA-----------
RUN yum -y install java-1.8.0-openjdk-1.8.0.91-1.b14.el6*
RUN mv /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.91-1.b14.el6.x86_64/jre /usr/local/jdk
ENV JAVA_HOME /usr/local/jdk

RUN yum -y install tar

#--------ANT------------
RUN curl http://archive.apache.org/dist/ant/binaries/apache-ant-1.9.7-bin.tar.gz --output /usr/ant.tar.gz
RUN tar -xzf /usr/ant.tar.gz
RUN chmod 777 /apache-ant-1.9.7/*
ENV ANT_HOME /apache-ant-1.9.7

ENV PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH
RUN java -version
RUN ant -version

#--------SHIBBOLETH------------
RUN curl https://shibboleth.net/downloads/identity-provider/2.4.0/shibboleth-identityprovider-2.4.0-bin.tar.gz --output /usr/shibboleth.tar.gz
#COPY src/shibboleth.tar.gz /usr/shibboleth.tar.gz
RUN tar -xzf /usr/shibboleth.tar.gz
RUN chmod 777 /shibboleth-identityprovider-2.4.0/*
COPY src/build.xml /shibboleth-identityprovider-2.4.0/src/installer/resources/build.xml
COPY src/install.properties /shibboleth-identityprovider-2.4.0/src/installer/resources/install.properties
COPY src/install.sh /shibboleth-identityprovider-2.4.0/install.sh

RUN mkdir /opt/shibboleth-idp/
RUN chmod 777 /opt/*
RUN cd /shibboleth-identityprovider-2.4.0 && ./install.sh
COPY src/conf/* /opt/shibboleth-idp/conf/
COPY src/spring_saml_metadata.xml /opt/shibboleth-idp/metadata/spring_saml_metadata.xml
RUN sed -i "s/shibboleth.com\//shibboleth.com:8443\//g" /opt/shibboleth-idp/metadata/idp-metadata.xml
RUN sed -i "s/:8443/:8446/g" /opt/shibboleth-idp/metadata/idp-metadata.xml
RUN sed -i "s/entityID=\"https:\/\/idp.shibboleth.com:8446/entityID=\"https:\/\/idp.shibboleth.com/g" /opt/shibboleth-idp/metadata/idp-metadata.xml

#----------TOMCAT----------------
RUN rm -f /usr/local/nom/etc/nom-tomcat-eportal/webapps/small-business.war
RUN rm -f /usr/local/nom/etc/nom-tomcat-eportal/webapps/eportal.war
RUN rm -f /usr/local/nom/etc/nom-tomcat-eportal/webapps/ROOT.war
RUN cp /opt/shibboleth-idp/war/idp.war /usr/local/nom/etc/nom-tomcat-eportal/webapps/idp.war
RUN chmod 777 /usr/local/nom/etc/nom-tomcat-eportal/webapps/*
COPY src/server.xml /usr/local/nom/etc/nom-tomcat-eportal/conf/server.xml
RUN cp /opt/shibboleth-idp/lib/endorsed/* /usr/local/nom/etc/nom-tomcat-eportal/libs/
COPY setenv.sh /usr/local/nom/etc/nom-tomcat-eportal/bin/setenv.sh






