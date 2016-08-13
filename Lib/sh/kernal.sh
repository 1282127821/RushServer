JDK=/usr/local/jdk1.6.0_24/bin
APPLICATIONS_HOME="/game/server/app"

JDK_OPTS="$1"
APPLICATION_NAME="$2"
MAIN_CLASS="$3"
SVR_TYPE="$4"
SVR_ID="$5"

LOCAL_PATH="$APPLICATIONS_HOME"/"$APPLICATION_NAME"
APPLICATION_CONFIG="$LOCAL_PATH"/config/config.properties
SERVER_ID="$LOCAL_PATH"/server.pid

PATH=$CLASSPATH
for i in $LOCAL_PATH/lib/*.jar;
do
    PATH="$PATH":$i
done

case "$6" in
    start)
         "$JDK"/java -server $JDK_OPTS -cp $PATH $MAIN_CLASS $APPLICATION_CONFIG "$7" &
         echo $! > $SERVER_ID
         PROCESS_ID=`/bin/cat $SERVER_ID`
         if [ "$PROCESS_ID" ]
         then
             echo "the $APPLICATION_NAME started" "$7"
         else
             /bin/rm -rf $SERVER_ID
             echo "miss config file path"
             echo "eg. sh gateway_server.sh start $7"
         fi
    ;;
    stop)
        echo "the $APPLICATION_NAME begin stopping in 5 minutes."
        "$JDK"/java -cp $PATH com.star.light.StopServer $APPLICATION_CONFIG $SVR_TYPE $SVR_ID "stop" &
    ;;
    stopnow)
        "$JDK"/java -cp $PATH com.star.light.StopServer $APPLICATION_CONFIG $SVR_TYPE $SVR_ID "stopnow" &
    ;;
    cmd)
        "$JDK"/java -cp $PATH com.star.light.AdminCmdRequestor $APPLICATION_CONFIG $SVR_TYPE $SVR_ID
    ;;
    allcmd)
        "$JDK"/java -cp $PATH com.star.light.AdminCmdRequestor
    ;;
    kill)
        PROCESS_ID=`/bin/cat $SERVER_ID`
        if [ "$PROCESS_ID" ]
        then
            echo "the $APPLICATION_NAME begin killing"
            /bin/rm -rf $SERVER_ID
            kill -9 $PROCESS_ID
            echo "the $APPLICATION_NAME begin killed"
        else
            echo "the $APPLICATION_NAME is not running"
        fi
    ;;
    *)
        echo "Usage: $0 (start|stop|stopall|cmd|allcmd|status|kill)"
esac
exit 0