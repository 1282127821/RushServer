package com.executor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import com.util.Log;

public class CmdTaskQueue
{
	/**
	 * 执行Task的线程池
	 */
	private ExecutorPool threadPool;

	/**
	 * 任务队列。队头元素是正在执行的任务
	 */
	private Queue<AbstractTask> taskQueue; // TODO:LZGLZG是否可以考虑使用RingBuffer，减少中间的内存分配

	/**
	 * 运行锁，用于确保同时最多只能有一个任务在执行。任务队列本身是线程安全的
	 */
	private ReentrantLock runningLock;

	public CmdTaskQueue(ExecutorPool pool)
	{
		this.threadPool = pool;
		this.taskQueue = new LinkedList<AbstractTask>();
		this.runningLock = new ReentrantLock();
	}

	/**
	 * 往任务队列中添加任务。
	 */
	public void add(AbstractTask task)
	{
		this.runningLock.lock();
		try
		{
			if (this.taskQueue.isEmpty())
			{
				this.taskQueue.add(task);
				// 没有任务在执行，开始执行新添加的。
				this.threadPool.submit(task);
			}
			else
			{
				// 有任务正在执行，将新任务添加到队列中，等待执行。
				this.taskQueue.add(task);
			}
		}
		finally
		{
			this.runningLock.unlock();
		}
	}

	/**
	 * 添加延时任务
	 */
	public void addDelayTask(AbstractDelayTask task)
	{
		threadPool.addDelayTask(task);
	}

	/**
	 * 完成一个任务，任务完成的时候，必须调用本方法来驱动后续的任务
	 */
	public void complete()
	{
		this.runningLock.lock();
		try
		{
			// 移除已经完成的任务。
			this.taskQueue.remove();
			// 完成一个任务后，如果还有任务，则继续执行。
			if (!this.taskQueue.isEmpty())
			{
				this.threadPool.submit(this.taskQueue.peek());
			}
		}
		catch (Exception e)
		{
			Log.error("complete error", e);
		}
		finally
		{
			this.runningLock.unlock();
		}
	}

	public void clear()
	{
		try
		{
			runningLock.lock();
			taskQueue.clear();
		}
		finally
		{
			runningLock.unlock();
		}
	}

	public int getQueueSize()
	{
		return taskQueue.size();
	}
}
