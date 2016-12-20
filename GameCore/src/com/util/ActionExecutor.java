//package com.util;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import com.execaction.Executor;
//
///**
// * 任务执行器
// * 
// */
//public class ActionExecutor extends Executor
//{
//	private ScheduledExecutorService scheduler; // 延迟执行处理器
//
//	/**
//	 * 创建任务执行器
//	 * 
//	 * @param name
//	 *            名称
//	 * @param corePoolSize
//	 *            核心线程数(保持运行数)
//	 * 
//	 * @param maximumPoolSize
//	 *            最大线程数
//	 */
//	public ActionExecutor(String name, int corePoolSize, int maximumPoolSize)
//	{
//		super(name, corePoolSize, maximumPoolSize);
//		scheduler = Executors.newScheduledThreadPool(1, new DefaultThreadFactory(name + "-Delay")); // 创建一个线程处理延迟
//	}
//
//	/** 添加延迟任务 **/
//	protected void addDelayAction(final Action action)
//	{
//		long delay = action.getDelay();
//		if (delay <= 0)
//		{
//			action.getQueue().enqueue0(action); // 加入执行队列
//			return;
//		}
//		// 延迟外壳封装
//		Runnable r = new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				action.getQueue().enqueue0(action); // 加入执行队列
//			}
//		};
//		scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
//	}
//
//	public void stop()
//	{
//		scheduler.shutdownNow();
//		super.stop();
//	}
//
//}
