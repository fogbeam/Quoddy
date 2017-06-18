#!/bin/sh

rm /opt/fogcutter/quoddy/quoddy.log

# psql -U postgres -d quoddy -f sql/clean_rss.sql

grails clean; grails -Dserver.port=8180 -Dquoddy.home=/opt/fogcutter/quoddy run-app

