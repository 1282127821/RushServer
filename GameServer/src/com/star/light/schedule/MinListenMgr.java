package com.star.light.schedule;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.star.light.player.PlayerMgr;
import com.star.light.player.WorldMgr;
import com.star.light.room.RoomMgr;
import com.star.light.util.GameLog;
import com.star.light.util.ThreadNameFactory;

/**
 * 分钟监视
 */
public final class MinListenMgr {
	public static final ThreadNameFactory fn = new ThreadNameFactory("MinListen", false);
	private static MinListenMgr minListenMgr = new MinListenMgr();
	private ExecutorService listenExecutor;
	private ScheduledExecutorService scheduledExecutorService;

	public static Runnable getThread(Runnable runnable, String threadName) {
		return fn.newThread(runnable, threadName);
	}

	public static MinListenMgr getInstance() {
		return minListenMgr;
	}

	public void stop() {
		GameLog.info("the MinListenMgr is stop...");
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
		if (listenExecutor != null) {
			listenExecutor.shutdown();
		}
	}

	public void init() {
		// 初始化奖励定时池
		int corePoolSize = 3;
		listenExecutor = Executors.newScheduledThreadPool(corePoolSize, MinListenMgr.fn);
		// 定时检查
		scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadNameFactory("timeExecute", false));
	}
}

/**
 * 定时触发器线程
 */
class MinTrigerTimerThread implements Runnable {
	private ExecutorService listenExecutor;
	private int hour;
	private int minute;
	
	private Runnable executeIn5Thread = MinListenMgr.getThread(new Runnable() {
		@Override
		public void run() {
			try {
				// 玩家重置
				WorldMgr.resetData();

				// 清空玩家缓存的离线信息
				PlayerMgr.clearData();

				RoomMgr.getInstance().resetRooms();
			} catch (Exception e) {
				GameLog.error("5点事件", e);
			}
		}
	}, " 5点事件");

	public MinTrigerTimerThread(ExecutorService listenExecutor) {
		this.listenExecutor = listenExecutor;
	}

	@Override
	public void run() {
		try {
			Calendar cal = Calendar.getInstance();
			this.hour = cal.get(Calendar.HOUR_OF_DAY);
			this.minute = cal.get(Calendar.MINUTE);

			// 5点事件
			if (hour == 5 && minute == 0) {
				listenExecutor.execute(executeIn5Thread);
			}
		
		} catch (Exception e) {
			GameLog.error("MinTrigerTimerThread錯誤", e);
		}
	}
}