#!/bin/sh

grails clean; grails -Dserver.port=8180 -Dquoddy.home=/opt/fogcutter/quoddy run-app

