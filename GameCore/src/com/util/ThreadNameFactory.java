package com.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

public class ThreadNameFactory implements ThreadFactory, UncaughtExceptionHandler
{
	/**
	 * 是否为后台线程
	 */
	private boolean daemon;

	/**
	 * 线程名
	 */
	private String threadName;

	/**
	 * 默认构造函数，threadName 线程名前缀 daemon 是否为后台线程
	 */
	public ThreadNameFactory(String threadName, boolean daemon)
	{
		this.threadName = threadName;
		this.daemon = daemon;
	}

	public ThreadNameFactory(String threadName)
	{
		this(threadName, false);
	}

	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(r, this.threadName);
		t.setDaemon(this.daemon);
		t.setUncaughtExceptionHandler(this);
		return t;
	}

	public void uncaughtException(Thread thread, Throwable throwable)
	{
		Log.error("Uncaught Exception in thread " + thread.getName(), throwable);
	}
}
