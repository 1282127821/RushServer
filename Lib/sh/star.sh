APPLICATIONS_HOME="/game/server/app"

case "$1" in
    start)
        sh $APPLICATIONS_HOME/castle_server.sh start "$2"
    ;;
    
	stop)
	    sh $APPLICATIONS_HOME/castle_server.sh stop
	;;
	
	stopnow)
	    sh $APPLICATIONS_HOME/castle_server.sh stopnow
    ;;
    
	kill)
	    sh $APPLICATIONS_HOME/castle_server.sh kill
	;;
    *)
        echo "Usage: $0 (start|kill)"
esac
exit 0