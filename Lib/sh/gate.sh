APPLICATIONS_HOME="/game/server/app"

case "$1" in
    start)
        sh $APPLICATIONS_HOME/gateway1.sh start "$2"
        sleep 10
        sh $APPLICATIONS_HOME/gateway2.sh start "$2"
        sleep 10
        sh $APPLICATIONS_HOME/gateway3.sh start "$2"
		;;
	
    stop)
        sh $APPLICATIONS_HOME/gateway1.sh stop
        sh $APPLICATIONS_HOME/gateway2.sh stop
        sh $APPLICATIONS_HOME/gateway3.sh stop
        ;;
		
    stopnow) 
        sh $APPLICATIONS_HOME/gateway1.sh stopnow
        sh $APPLICATIONS_HOME/gateway2.sh stopnow
        sh $APPLICATIONS_HOME/gateway3.sh stopnow
		;;
	
    kill)  
        sh $APPLICATIONS_HOME/gateway1.sh kill
        sh $APPLICATIONS_HOME/gateway2.sh kill
        sh $APPLICATIONS_HOME/gateway3.sh kill
        ;;
    *)
        echo "Usage: $0 (start|kill)"
esac
exit 0