#!/bin/bash

# Install script for installing Quoddy. For now the main focus is on the "test" scenario
# to support our development of an automated suite of "smoke tests" to run on the Jenkins CI
# server.


function installDefault {
    echo "Not implemented yet. Aborting."
    exit
}

function installTest {
    echo "Proceeding with TEST install"

    # make sure the test db instance exists
    $(dropdb -U postgres quoddy_test)
    
    createdb -U postgres quoddy_test

    # apply the Quartz db ddl scripts
    psql -U postgres -h localhost -d quoddy_test -f sql/quartz_sql.sql    
    psql -U postgres -h localhost -d quoddy_test -f sql/quartz_sql_new.sql
    
    echo "Done"
    exit
}



case $1 in
    "default")
        echo "Doing default install!"
        installDefault
        ;;
    "test")
        echo "Doing test install!"
        installTest
        ;;
    *)
        echo "Unknown installation profile!"
        ;;
esac

