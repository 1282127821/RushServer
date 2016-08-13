#初始堆, 最大堆, 年轻代, 每个线程的堆栈大小, 初始持久代, 持久代最大值内存大小
JDK_OPTS="-Xms512m -Xmx512m -Xmn128m -Xss1m -XX:PermSize=64m -XX:MaxPermSize=64m"
#Eden区与Survivor区的大小比值
#JDK_OPTS=$JDK_OPTS" -XX:SurvivorRatio=1"
#设置年轻代为并行收集
JDK_OPTS=$JDK_OPTS" -XX:+UseParNewGC"
#使用CMS内存收集
JDK_OPTS=$JDK_OPTS" -XX:+UseConcMarkSweepGC"
#多少次后进行内存压缩
JDK_OPTS=$JDK_OPTS" -XX:CMSFullGCsBeforeCompaction=5"
#在FULL GC的时候, 对年老代压缩
JDK_OPTS=$JDK_OPTS" -XX:+UseCMSCompactAtFullCollection"
#设置垃圾回收时间占程序运行的时间百分比 1/(1 + n)
JDK_OPTS=$JDK_OPTS" -XX:GCTimeRatio=19"
#禁用类垃圾回收
JDK_OPTS=$JDK_OPTS" -Xnoclassgc"
#多少百分比后开始使用cms收集作为垃圾回收, 满足(Xmx - Xmn) * (100 - CMSInitiatingOccupancyFraction) / 100 >= Xmn
JDK_OPTS=$JDK_OPTS" -XX:CMSInitiatingOccupancyFraction=60"
#每兆堆空闲空间中SoftReference的存活时间
JDK_OPTS=$JDK_OPTS" -XX:SoftRefLRUPolicyMSPerMB=0"
#EPoll网络连接机制设定
JDK_OPTS=$JDK_OPTS" -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider"
#JMX远程监听端口设置
JDK_OPTS=$JDK_OPTS" -Dcom.sun.management.jmxremote.port=8903 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=10.10.4.33"
JDK_OPTS=$JDK_OPTS" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8803"
#GC日志
JDK_OPTS=$JDK_OPTS" -XX:+PrintClassHistogram -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
JDK_OPTS=$JDK_OPTS" -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime"
JDK_OPTS=$JDK_OPTS" -XX:+PrintHeapAtGC -Xloggc:/game/server/app/gateway1/log/gc.log"

APPLICATION_NAME=gateway1
MAIN_CLASS="com.star.light.GatewayServer"

EXECUTABLE=/game/server/app/kernal.sh
exec "$EXECUTABLE" "$JDK_OPTS" "$APPLICATION_NAME" "$MAIN_CLASS" 2 0 "$@" "$@"
