#!/usr/bin/env bash
gradle war
scp -P 22  build/libs/youngScholar-1.0-SNAPSHOT.war root@123.206.78.162:/home/data/tomcat/webapps/ROOT.war