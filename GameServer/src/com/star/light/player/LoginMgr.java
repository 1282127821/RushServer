package com.star.light.player;

import java.util.Timer;
import java.util.TimerTask;

public class LoginMgr extends TimerTask {
	// TODO:LZGLZG这个考虑删除掉
	// 不再需要排队的，提高服务器的负载，争取能够更多的玩家容纳才是王道，给玩家等待队列的游戏基本很少有，体验感也不好
	/**
	 * 服务器上限人数
	 */
	private static int onlineMan = 4000;

	/**
	 * 扫描频率(5秒=1000*5)
	 */
	public static int scanTime = 1000 * 5;

	/**
	 * 清除过期数据（120分钟=120*60*1000）
	 */
	private static int clearTime = 120 * 1000 * 60;

	/**
	 * 同步在线用户频率（1分钟=1000*60）
	 */
	private static int synOnlineTime = 1000 * 60;

	/**
	 * 登陆列表
	 */
	private static AbstractCache loginCache;

	/**
	 * 在线列表
	 */
	private static AbstractCache onlineCache;

	public static boolean init() {
		loginCache = new LoginsCache(scanTime, clearTime, false);
		onlineCache = new OnlinesCache(synOnlineTime, clearTime, false);
		new Timer("LoginMgr").schedule(new LoginMgr(), 0, scanTime);
		setWaiteOpen(false, 20);
		setOnlineMan(onlineMan);
		return true;
	}

	public static void setWaiteOpen(boolean isOpen, int maxWaite) {
		loginCache.setIsOpen(isOpen);
		LoginsCache.MAX_WAITE = maxWaite;
	}

	public static void setOnlineMan(int maxMan) {
		if (maxMan > 0) {
			onlineCache.setIsOpen(true);
			onlineMan = maxMan;
		} else {
			onlineCache.setIsOpen(false);
		}
	}

	public static int getOnlineMan() {
		return onlineMan;
	}

	public static boolean isLoginWaitOpen() {
		return loginCache.getIsOpen();
	}

	// 注册在线Key值
	public static void createOnline(String key, long accountId) {
		onlineCache.add(key, accountId);
	}

	public static String getKey(long accountId) {
		return ((OnlinesCache) onlineCache).getKey(accountId);
	}

	// 检测登陆
	public static LoginMsg checkLogin(String name) {
		LoginMsg info = (LoginMsg) loginCache.check(name, "");
		return info;
	}

	// 登陆人数上限
	public static LoginMsg checkQueue(String name) {
		LoginMsg info = ((LoginsCache) loginCache).checkQueue(name);
		return info;
	}

	// 检测在线
	public static boolean checkOnline(long userId, String key) {
		return (Boolean) onlineCache.check(userId, key);
	}

	@Override
	public void run() {
		int currentCount = onlineCache.getManCount();
		if (currentCount > onlineMan) {
			// 打开排队
			if (!loginCache.getIsOpen()) {
				setWaiteOpen(true, LoginsCache.MAX_WAITE);
			}
			loginCache.scan(true);
		} else {
			// 关闭排队
			if (loginCache.getIsOpen() && onlineMan - currentCount > 100) {
				setWaiteOpen(false, LoginsCache.MAX_WAITE);
			}
			loginCache.scan(false);
		}
		onlineCache.clear();
		onlineCache.scan(true);
	}
}
