#!/bin/sh

rm /opt/fogcutter/quoddy/quoddy.log

# psql -U postgres -d quoddy -f sql/clean_rss.sql

./grailsw clean; ./grailsw -Dserver.port=8383 -Dquoddy.home=/opt/fogcutter/quoddy -Dspring.config.location=/opt/fogcutter/quoddy/ -Drebuild.indexes=false -Dfogbeam.devmode=true -Dspring.profiles.active=test -Dgrails.env=test run-app $@

