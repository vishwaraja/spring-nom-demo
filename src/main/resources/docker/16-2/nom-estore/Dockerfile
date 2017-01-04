FROM nominum/nom-estore-centos6_head:latest

COPY set_ip.sh /usr/local/nom/sbin/set_ip.sh
COPY run_custom_templates.sh /usr/local/nom/sbin/run_custom_templates.sh
COPY supervisord.conf /etc/supervisord.conf
COPY generate_keys.sh /usr/local/nom/sbin/generate_keys.sh
COPY reports /var/nom/nom-estore/reports/common
COPY useragent_regex /var/nom/nom-estore/useragent_regex/common
RUN ln -s /var/nom/nom-estore/dashboard/ews-data/common /var/nom/ews-data
RUN curl -s https://csp.nominum.com/ews/ews/ews-data > /var/nom/ews-data/ews-data.json
RUN cp /usr/local/nom/etc/nom-nginx/nginx.conf.example /usr/local/nom/etc/nom-nginx/nginx.conf
RUN sed -i "s/https:\/\/estore/http:\/\/estore:8445/g" /usr/local/nom/share/nom-estore/customTemplates.json
RUN sed -i "s/https:\/\/estore/http:\/\/estore:8445/g" /usr/local/nom/share/nom-estore/advancedReporting.json
RUN sed -i "s/https:\/\/estore/http:\/\/estore:8445/g" /usr/local/nom/share/nom-estore/ewsDashboard.json
RUN sed -i "s/https:\/\/estore/http:\/\/estore:8445/g" /usr/local/nom/share/nom-estore/userAgentRegex.json
RUN sed -i "s/CTU/Docker/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/server\.host=.*/server\.host=smtp\.gmail\.com/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/server\.port=.*/server\.port=587/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/sender\.username=.*/sender\.username=nomtest321@gmail\.com/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/sender\.password=.*/sender\.password=nom987678/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/mail\.recipient=.*/mail\.recipient=nomtest321@gmail\.com/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/mail\.enable\.method\.list=.*/mail\.enable\.method\.list=upload, generate, fail/g" /usr/local/nom/etc/nom-estore/custom_templates.properties
RUN sed -i "s/3g/512m/g" /usr/local/nom/etc/nom-tomcat-eportal/nom-tomcat-eportal.conf
RUN sed -i "s/1g/256m/g" /usr/local/nom/etc/nom-tomcat-eportal/nom-tomcat-eportal.conf
#RUN /usr/local/nom/sbin/set_ip.sh
COPY universal-eval.license /usr/local/nom/etc/
