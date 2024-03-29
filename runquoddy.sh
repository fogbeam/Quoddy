#!/bin/sh

rm /opt/fogcutter/quoddy/quoddy.log

# psql -U postgres -d quoddy -f sql/clean_rss.sql

./grailsw clean

./gradlew assemble

./grailsw -Dserver.port=8383 -Dquoddy.home=/opt/fogcutter/quoddy -Dspring.config.location=/opt/fogcutter/quoddy/ -Drebuild.indexes=false -Dfogbeam.devmode=true -Dspring.profiles.active=test -Dgrails.env=test run-app $@ &

while ! nc -z localhost 8383; do
  sleep 0.1 # wait for 1/10 of the second before check again                                                                 
done

echo "Quoddy started, writing pidfile"

lsof -i6TCP:8383 -sTCP:LISTEN -t > pidfile

