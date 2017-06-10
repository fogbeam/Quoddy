#!/bin/bash


service tomcat stop
sleep 5s
service tomcat stop
sleep 5s

rm -rf /usr/share/tomcat/webapps/quoddy/
rm -f /usr/share/tomcat/webapps/quoddy.war
rm -f /usr/share/tomcat/logs/*
rm -f /opt/fogcutter/quoddy/quoddy.log

git pull

grails clean

grails war

rm -f /usr/share/tomcat/logs/*
rm -f /opt/fogcutter/quoddy/quoddy.log

cp target/quoddy-0.0.1.war /usr/share/tomcat/webapps/quoddy.war

service tomcat start
