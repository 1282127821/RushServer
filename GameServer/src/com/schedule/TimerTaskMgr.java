package com.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import com.player.WorldMgr;
import com.util.GameLog;
import com.util.TimeUtil;

public class TimerTaskMgr
{
	// 最小时间隔1分钟
	protected static final int MINTIME = 1000 * 60;
	private static Timer scanTimer;
	private static Timer saveUserDataTimer;
	private static TimerTask saveUserData;
	private static TimerTask checkDBPool;

	public static void init()
	{
		// 设置启动时间
		Date beginDate = TimeUtil.addSystemCurTime(Calendar.SECOND, ThreadLocalRandom.current().nextInt(5) * 60);

		// 保存用户数据
		saveUserDataTimer = new Timer("SaveUserDataTimer");
		saveUserData = new SaveUserData();
		saveUserDataTimer.schedule(saveUserData, beginDate, MINTIME * 5);

		// 每天凌晨0点开始检查在线用户和前一天等级分布及流失统计，并作当天的登陆记录
		scanTimer = new Timer("ScanTimer");
		// 检查DBPool是否正常
		checkDBPool = new DBPool();
		scanTimer.schedule(checkDBPool, MINTIME * 3, MINTIME * 5);
	}
}

abstract class Task extends TimerTask
{
	private String name;

	public Task(String name)
	{
		this.name = "定时器任务-" + name;
	}

	@Override
	public void run()
	{
		try
		{
			exec();
		}
		catch (Exception e)
		{
			GameLog.error(name + "错误", e);
		}
	}

	public abstract void exec();
}

class SaveUserData extends Task
{
	public SaveUserData()
	{
		super("保存用户数据");
	}

	@Override
	public void exec()
	{
		WorldMgr.save();
	}
}

class DBPool extends Task
{
	public DBPool()
	{
		super("检查连接池状态");
	}

	@Override
	public void exec()
	{
		// DBPoolMgr.getInstaqnce().checkConnectionPool();
	}
}