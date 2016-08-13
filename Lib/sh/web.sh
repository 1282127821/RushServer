#!/bin/bash

case "$1" in
    start)
        sh /usr/local/tomcat1/bin/catalina.sh start
        sleep 10
        sh /usr/local/tomcat2/bin/catalina.sh start
        sleep 10
        sh /usr/local/tomcat3/bin/catalina.sh start

        ;;
    stop)
        sh /usr/local/tomcat1/bin/catalina.sh stop
        rm -rf  /usr/local/tomcat1/work/Catalina/*
        sh /usr/local/tomcat2/bin/catalina.sh stop
        rm -rf  /usr/local/tomcat2/work/Catalina/*
        sh /usr/local/tomcat3/bin/catalina.sh stop
        rm -rf  /usr/local/tomcat3/work/Catalina/*

        ;;
    *)
        echo "Usage: $0 (start|stop)"
esac
exit 0
