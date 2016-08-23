package com.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在单进程内生成游戏中唯一的Id，比如玩家 装备 道具等等需要使用唯一Id来标识的。
 * 产生算法：根据对应的服务器Id（目前是保留16位，也就是一共可以开65536个服，对于绝大部分游戏来说都是足够的），
 * 根据系统当前的毫秒数，在每一毫秒中可以产生65535个值，对于绝大部分游戏的并发程度来说也是足够的。
 */
public final class GUIDUitl {
	private static AtomicInteger id = new AtomicInteger(0);

	public static long getId() {
		return 1;
		//return ((Config.SEVER_NO * 1L & 0xFFFF) << 48) | ((System.currentTimeMillis() / 1000L & 0xFFFFFFFF) << 16) | (id.addAndGet(1) & 0xFFFF);
	}
}
