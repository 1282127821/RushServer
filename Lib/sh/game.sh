APPLICATIONS_HOME="/game/server/app"
case "$1" in
    start)
        sh $APPLICATIONS_HOME/star.sh start
        sleep 30
        sh $APPLICATIONS_HOME/gate.sh start
        sleep 30
        jps
        sh $APPLICATIONS_HOME/web.sh start
        sleep 5
        echo "---------------start success!----------------------" 
        ;;
    stop)
    		sh $APPLICATIONS_HOME/web.sh stop
    		sleep 5
    		sh $APPLICATIONS_HOME/gate.sh stopnow
    		sleep 5
    		sh $APPLICATIONS_HOME/star.sh stopnow
                sleep 10
                jps
                ps -ef|grep tomcat
                echo "---------------stop success!----------------------"
        ;;
    *)
        echo "Usage: $0 (start|stop)"
esac
exit 0
