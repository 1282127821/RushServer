//package com.util.timer;
//
//import java.util.Date;
//
//import com.util.TimerTaskMgr;
//
///**
// * 按时间执行的定时器<br>
// * 所以长周期（超过24H）的定时全部使用这种定时器
// * 
// */
//public abstract class FixedTimerTask
//{
//	private int id;
//	private String name;
//	private int dayOfWeek;
//	private int dayOfMonth;
//	private int hour;
//	private int min;
//	private boolean isFixed;
//	private ActionQueue queue;
//	private Action action;
//
//	public FixedTimerTask(int id, String name, int hour, int min)
//	{
//		this.id = id;
//		this.name = name;
//		this.hour = hour;
//		this.min = min;
//		action = new Action()
//		{
//			public void execute() throws Exception
//			{
//				exec();
//			}
//
//			protected String getName()
//			{
//				return FixedTimerTask.this.getClass().getName() + "[" + FixedTimerTask.this.name + "]";
//			}
//
//		};
//		queue = TimerTaskMgr.createActionQueue();
//	}
//
//	/**
//	 * 
//	 * @param id
//	 * @param name
//	 * @param dayOfWeek
//	 *            取Calendar的常量
//	 * @param hour
//	 * @param min
//	 * @param isFixed
//	 */
//	public FixedTimerTask(int id, String name, int dayOfMonth, int dayOfWeek, int hour, int min)
//	{
//		this(id, name, hour, min);
//		this.dayOfWeek = dayOfWeek;
//		this.dayOfMonth = dayOfMonth;
//	}
//
//	/**
//	 * 为什么把setUpdateTime拿到action外面？</br>
//	 * 1st.当服务器时间出错的时候，会导致task被执行多次（因为是否执行是在外部判断,具体执行和更改时间是在action中进行,
//	 * 所以如果action执行慢了会导致压入多个action
//	 * 2nd.如果exec抛出异常,则此任务算作未执行过,然而我们并不能保证exec是在哪一步出错，
//	 * 也就是说这里面可能有一部分东西是可以执行成功的，这样就会导致一种情况：可以执行成功的那一部分东西一直在重复执行,
//	 * 不成功的则永远不成功.所以这里直接简单粗暴,只要进来就算执行过,要错一起错,错了就不会再执行第二次
//	 */
//	public void execute(final Date updateTime)
//	{
//		try
//		{
//			info.setUpdateTime(updateTime);
//			save();
//			queue.enqueue(action);
//		}
//		catch (Exception e)
//		{
//			Log.error("定时器执行异常, Id : " + id);
//		}
//	}
//
//	protected abstract void exec() throws Exception;
//
//	public int getId()
//	{
//		return id;
//	}
//
//	public String getName()
//	{
//		return name;
//	}
//
//	public int getDayOfWeek()
//	{
//		return dayOfWeek;
//	}
//
//	public int getDayOfMonth()
//	{
//		return dayOfMonth;
//	}
//
//	public int getHour()
//	{
//		return hour;
//	}
//
//	public int getMin()
//	{
//		return min;
//	}
//
//	public boolean isFixed()
//	{
//		return isFixed;
//	}
//
//	public Date getUpdateTime()
//	{
//		return info.getUpdateTime();
//	}
//
//	public void save()
//	{
//		if (info.getOp() == Option.INSERT)
//		{
//			DaoMgr.getTimerJobInfoDao().addTimerJobInfo(info);
//		}
//		else if (info.getOp() == Option.UPDATE)
//		{
//			DaoMgr.getTimerJobInfoDao().updateTimerJobInfo(info);
//		}
//	}
//
//}
