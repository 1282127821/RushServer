//package com.util;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
//import com.execaction.ActionQueue;
//import com.util.timer.FixedTimerTask;
//import com.util.timer.PeriodTimerTask;
//
///**
// * 定时器管理类
// */
//public class TimerTaskMgr
//{
//	private static ActionExecutor executor;
//	private static Timer timer;
//	private static FixedTimerScanner fixedTimerScanner;
//
//	public static boolean init()
//	{
//		timer = new Timer("Timer");
//		// 定时扫面器
//		fixedTimerScanner = new FixedTimerScanner();
//		timer.scheduleAtFixedRate(fixedTimerScanner, TimeUtil.nextMinDelay(), 60000);
//		// 创建线程池
//		int nThreads = Runtime.getRuntime().availableProcessors();
//		executor = new ActionExecutor("TimerTask-Action", nThreads, nThreads);
//		return true;
//	}
//
//	/** 注册周期任务 **/
//	public static void register(PeriodTimerTask task)
//	{
//		// 检测初始化
//		if (timer == null)
//		{
//			GameLog.error("定时器尚未初始化: task" + task);
//			return;
//		}
//
//		// 加入定时器
//		if (task.getFirstTime() == null)
//		{
//			timer.scheduleAtFixedRate(task, task.getDelay(), task.getPeriod());
//		}
//		else
//		{
//			timer.scheduleAtFixedRate(task, task.getFirstTime(), task.getPeriod());
//		}
//	}
//
//	/** 注册定期任务 **/
//	public static void register(FixedTimerTask task)
//	{
//		fixedTimerScanner.addTask(task);
//	}
//
//	/** 创建任务队列 **/
//	public static ActionQueue createActionQueue()
//	{
//		return new ActionQueue(executor);
//	}
//
//	/** 停止定时器 **/
//	public static void stop()
//	{
//		try
//		{
//			timer.cancel();
//			executor.stop();
//		}
//		catch (Exception e)
//		{
//			Log.error("关闭定时器异常", e);
//		}
//	}
//
//	/** 定时扫描器 **/
//	static class FixedTimerScanner extends AbstractTimerTask
//	{
//		private List<FixedTimerTask> tasks; // 任务队列
//		private ReadWriteLock lock;
//
//		protected FixedTimerScanner()
//		{
//			super("FixedTimerScanner");
//			tasks = new ArrayList<>();
//			lock = new ReentrantReadWriteLock();
//		}
//
//		/** 根据ID获取任务 **/
//		public FixedTimerTask get(int id)
//		{
//			try
//			{
//				lock.readLock().lock();
//				for (FixedTimerTask temps : tasks)
//				{
//					if (temps.getId() == id)
//					{
//						return temps;
//					}
//				}
//			}
//			finally
//			{
//				lock.readLock().unlock();
//			}
//			return null;
//		}
//
//		public boolean addTask(FixedTimerTask task)
//		{
//			// 检测是否存在相同ID的任务
//			FixedTimerTask find = get(task.getId());
//			if (find != null)
//			{
//				Log.error("定时任务Id重复! Id : " + task.getId());
//				return false;
//			}
//
//			// 添加到列表
//			try
//			{
//				lock.writeLock().lock();
//				tasks.add(task);
//			}
//			finally
//			{
//				lock.writeLock().unlock();
//			}
//			return true;
//		}
//
//		@Override
//		protected void execute() throws Exception
//		{
//			// 获取当前所有任务
//			lock.readLock().lock();
//			List<FixedTimerTask> tasks0 = new ArrayList<>(tasks);
//			lock.readLock().unlock();
//
//			// 执行定时器
//			Date curTime = new Date();
//			for (FixedTimerTask task : tasks0)
//			{
//				// 计算这个星期的天数
//				Calendar cale = Calendar.getInstance();
//				cale.setFirstDayOfWeek(Calendar.MONDAY);
//				if (task.getDayOfWeek() > 0)
//				{
//					cale.set(Calendar.DAY_OF_WEEK, task.getDayOfWeek());
//				}
//				else if (task.getDayOfMonth() > 0)
//				{
//					cale.set(Calendar.DAY_OF_MONTH, task.getDayOfMonth());
//				}
//				cale.set(Calendar.HOUR_OF_DAY, task.getHour());
//				cale.set(Calendar.MINUTE, task.getMin());
//				cale.set(Calendar.SECOND, 0);
//				cale.set(Calendar.MILLISECOND, 0);
//				// 算出对应时间
//				Date targetTime = cale.getTime();
//				Date lastUpdateTime = task.getUpdateTime();
//				if (lastUpdateTime != null)
//				{
//					if (lastUpdateTime.getTime() - targetTime.getTime() >= 0) // 已执行过
//					{
//						continue;
//					}
//				}
//				if (curTime.before(targetTime)) // 时间未到
//				{
//					continue;
//				}
//				task.execute(curTime);
//			}
//		}
//
//	}
//
//}
