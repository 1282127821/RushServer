package com.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.util.Log;
import com.util.ThreadNameFactory;
import com.util.TimeUtil;

public class ExecutorPool
{
	private static int CHECK_INTERVAL = 200;
	private ExecutorService service;
	private ExecutorService delayService;
	private List<AbstractDelayTask> delayList;
	private List<AbstractDelayTask> delayListTemp;

	public ExecutorPool(String poolName, int size)
	{
		service = Executors.newFixedThreadPool(size, new ThreadNameFactory(poolName));
		delayService = Executors.newSingleThreadExecutor();
		delayList = new ArrayList<AbstractDelayTask>();
		delayListTemp = new ArrayList<AbstractDelayTask>();

		delayService.submit(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					long begin = TimeUtil.getSysCurTimeMillis();
					checkDelayTask(begin);
					long interval = TimeUtil.getSysCurTimeMillis() - begin;
					if (interval < CHECK_INTERVAL)
					{
						try
						{
							Thread.sleep(CHECK_INTERVAL - interval);
						}
						catch (InterruptedException e)
						{
							Log.error("error", e);
						}
					}
					else
					{
						Log.warn("delay task check speed too much time. time:{}", interval);
					}
				}
			}
		});
	}

	public void submit(AbstractTask task)
	{
		service.submit(task);
	}

	public void submit(Runnable task)
	{
		service.submit(task);
	}

	public void addDelayTask(AbstractDelayTask action)
	{
		synchronized (delayList)
		{
			delayList.add(action);
		}
	}

	private void checkDelayTask(long time)
	{
		if (delayList.isEmpty())
			return;

		synchronized (delayList)
		{
			delayListTemp.addAll(delayList);
			delayList.clear();
		}

		List<AbstractDelayTask> list = new ArrayList<AbstractDelayTask>();
		for (AbstractDelayTask t : delayListTemp)
		{
			if (!t.checkDelayFinishAndExecute(time))
				list.add(t);
		}

		synchronized (delayList)
		{
			delayList.addAll(list);
		}

		list.clear();
		delayListTemp.clear();
	}

	public void shutdown()
	{
		delayList.clear();
		delayListTemp.clear();
		service.shutdownNow();
		delayService.shutdown();
	}
}