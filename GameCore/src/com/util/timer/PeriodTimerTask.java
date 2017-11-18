//package com.util.timer;
//
//import java.util.Date;
//
//import com.execaction.Action;
//import com.execaction.ActionQueue;
//
///**
// * 周期执行
// * 
// */
//public abstract class PeriodTimerTask extends AbstractTimerTask
//{
//	public static final long MINITES = 60 * 1000;
//	private Date firstTime; // 第一次开始时间(与延迟时间不共用)
//	private long delay; // 延迟多久开始
//	private long period; // 周期时间
//	private Action action;
//	private ActionQueue queue;
//	
//	protected int index;
//	protected int mod;
//
//	private PeriodTimerTask(final String name)
//	{
//		super(name);
//
//		index = 0;
//		mod = 5;
//
//		action = new Action()
//		{
//			public void execute() throws Exception
//			{
//				exec();
//				index++;
//			}
//
//			protected String getName()
//			{
//				return PeriodTimerTask.this.getClass().getName() + "[" + name + "]";
//			}
//		};
//		queue = TimerTaskMgr.createActionQueue();
//	}
//
//	/**
//	 * 周期定时器
//	 * 
//	 * @param name
//	 * @param firstTime
//	 *            第一次启动时间
//	 * @param period
//	 *            周期
//	 */
//	public PeriodTimerTask(String name, Date firstTime, long period)
//	{
//		this(name);
//		this.firstTime = firstTime;
//		this.period = period;
//	}
//
//	/**
//	 * 周期定时器
//	 * 
//	 * @param name
//	 * @param delay
//	 *            延迟时间
//	 * @param period
//	 *            周期
//	 */
//	public PeriodTimerTask(String name, long delay, long period)
//	{
//		this(name);
//		this.delay = delay;
//		this.period = period;
//	}
//
//	@Override
//	protected void execute() throws Exception
//	{
//		queue.enqueue(action);
//	}
//
//	protected abstract void exec() throws Exception;
//
//	public Date getFirstTime()
//	{
//		return firstTime;
//	}
//
//	public long getDelay()
//	{
//		return delay;
//	}
//
//	public long getPeriod()
//	{
//		return period;
//	}
//
//	protected int getIndex()
//	{
//		return index % mod;
//	}
//
//	protected int getMod()
//	{
//		return mod;
//	}
//}
