package com.rush.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadNameFactory implements ThreadFactory {

	AtomicInteger poolNumber = new AtomicInteger(1);
	ThreadGroup group;
	AtomicInteger threadNumber = new AtomicInteger(1);
	String namePrefix;
	boolean isDaemon;

	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(group, runnable, (new StringBuilder()).append(namePrefix).append(threadNumber.getAndIncrement()).toString(), 0L);
		thread.setDaemon(isDaemon);
		if (thread.getPriority() != 5)
			thread.setPriority(5);
		return thread;
	}
	
	public Thread newThread(Runnable runnable,String suffix ) {
		Thread thread = new Thread(group, runnable, (new StringBuilder()).append(namePrefix).append(suffix).toString(), 0L);
		thread.setDaemon(isDaemon);
		if (thread.getPriority() != 5)
			thread.setPriority(5);
		return thread;
	}

	public ThreadNameFactory(String prefix,boolean isDaemon) {
		this.isDaemon = isDaemon;
		SecurityManager securitymanager = System.getSecurityManager();
		group = securitymanager == null ? Thread.currentThread().getThreadGroup() : securitymanager.getThreadGroup();
		namePrefix = (new StringBuilder()).append("pool-").append(poolNumber.getAndIncrement()).append("-").append(prefix).append("-thread-").toString();
	}
}
