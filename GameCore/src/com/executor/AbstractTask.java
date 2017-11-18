package com.executor;

import com.util.Log;

public abstract class AbstractTask implements Runnable
{
	protected CmdTaskQueue queue; // 消息任务队列

	public AbstractTask(CmdTaskQueue queue)
	{
		this.queue = queue;
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
			Log.error("AbstractTask error", e);
		}
		finally
		{
			this.queue.complete();
		}
	}

	public abstract void execute();
}
