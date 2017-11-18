package com.util.timer;

import java.util.TimerTask;

import com.util.Log;

/**
 * 定时器任务基类
 * 
 */
public abstract class AbstractTimerTask extends TimerTask
{
	protected String name;

	protected AbstractTimerTask(String name)
	{
		name = "TimerTask : " + name;
	}

	@Override
	public void run()
	{
		try
		{
			execute();
		}
		catch (Exception e)
		{
			Log.error(name + "执行异常!", e);
		}
	}

	protected abstract void execute() throws Exception;
}