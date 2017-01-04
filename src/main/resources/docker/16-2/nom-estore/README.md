
nom-estore
==========

Launching Custom Templates Utility
==================================

1. Execute

   With docker-machine on the Mac, the custom template application is already running  

2. On Mac OS: open [https://192.168.99.100:8445](https://192.168.99.100:8445) (depends on your current docker-machine IP default) in browser.  
   On Linux: open [https://localhost:8445](https://localhost:8445) in browser.  
   Add security certificate into the browser exceptions.
   
3. Open N2 Reach / Templates / Custom Templates page on Apps Portal.

Checking email notification
===========================

By default, all email notification are sent from Gmail account to itself.  
Credentials:  
user: nomtest321@gmail.com  
password: nom987678  

To change these properties, update `/usr/local/nom/etc/nom-estore/custom_templates.properties` inside of compose_estore_1 container and restart nom-tomcat-eportal service.  

    $ docker exec -it compose_estore_1 /etc/init.d/nom-tomcat-eportal restart

Nominum SMTP server: smtp.nominum.com  


Troubleshooting
===============

<TROUBLESHOOTING>
