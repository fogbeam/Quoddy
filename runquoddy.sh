#!/bin/sh

rm /opt/fogcutter/quoddy/quoddy.log

# psql -U postgres -d quoddy -f sql/clean_rss.sql

grails clean; grails -Dserver.port=8383 -Dquoddy.home=/opt/fogcutter/quoddy -Dspring.config.location=/opt/fogcutter/quoddy/ run-app $@

# -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=java.lang,javax.security,java.util,org.apache.activemq,org.fusesource.hawtbuf,com.thoughtworks.xstream.mapper,com.fogbeam.quoddy,org.fogbeam.quoddy,org.grails.datastore.mapping.validation,org.springframework.validation,java.sql,org.hibernate.collection.internal,org.hibernate.proxy.pojo.javassist
