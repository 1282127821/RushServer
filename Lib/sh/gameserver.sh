#初始堆, 最大堆, 年轻代, 每个线程的堆栈大小, 初始持久代, 持久代最大值内存大小
JDK_OPTS="-Xms6144m -Xmx6144m -Xmn1024m -Xss1m -XX:PermSize=128M -XX:MaxPermSize=128M"
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
#JMX远程监听端口设置
JDK_OPTS="$JDK_OPTS -Dcom.sun.management.jmxremote.port=9988 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
JDK_OPTS=$JDK_OPTS" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8801"
#jprofile设置
#JDK_OPTS=$JDK_OPTS" -agentpath:/usr/local/jprofiler6/bin/linux-x64/libjprofilerti.so=port=7901"
#GC日志
JDK_OPTS=$JDK_OPTS" -XX:+PrintClassHistogram -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
JDK_OPTS=$JDK_OPTS" -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime"
JDK_OPTS=$JDK_OPTS" -XX:+PrintHeapAtGC -Xloggc:/game/server/app/castleserver/log/gc.log"

APPLICATION_NAME=castleserver
MAIN_CLASS="com.star.light.CastleServer"

EXECUTABLE=/game/server/app/kernal.sh
exec "$EXECUTABLE" "$JDK_OPTS" "$APPLICATION_NAME" "$MAIN_CLASS" 3 0 "$@" "$@"

