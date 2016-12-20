package com.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 根据twitter的snowflake算法生成唯一ID snowflake算法 64 位 0---0000000000 0000000000
 * 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 * 第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），
 * 然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
 * 其中datacenter标识位起始是机器位，机器ID其实是线程标识，可以同一一个10位来表示不同机器 参考文章
 * http://www.lanindex.com/twitter-snowflake%EF%BC%8C64%E4%BD%8D%E8%87%AA%E5%A2%9Eid%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3/
 * http://www.dengchuanhua.com/132.html
 * https://blog.twitter.com/2010/announcing-snowflake
 */
public final class IDWorker
{
	private final long workerId;
	/**
	 * 起始的时间，可以选定一个时间，选定之后就不能够进行更改（正式上线之后）
	 */
	private final static long twepoch = 1481770212418L;
	private long sequence = 0L;
	private final static long workerIdBits = 4L;
	private final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final static long sequenceBits = 10L;
	private final static long workerIdShift = sequenceBits;
	private final static long timestampLeftShift = sequenceBits + workerIdBits;
	private final static long sequenceMask = -1L ^ -1L << sequenceBits;
	private long lastTimestamp = -1L;

	public IDWorker(final long workerId)
	{
		if (workerId > maxWorkerId || workerId < 0)
		{
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId()
	{
		long timestamp = System.currentTimeMillis();
		if (this.lastTimestamp == timestamp)
		{
			this.sequence = (this.sequence + 1) & sequenceMask;
			if (this.sequence == 0)
			{
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		}
		else
		{
			this.sequence = 0;
		}

		if (timestamp < this.lastTimestamp)
		{
			try
			{
				throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << workerIdShift) | (this.sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp)
	{
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp)
		{
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	private static void testUniqueId()
	{
		final ConcurrentMap<Long, Long> keys = new ConcurrentHashMap<>();
		final int gameZoneId = 3;
		final IDWorker worker2 = new IDWorker(gameZoneId);
		for (int i = 0; i < 10; i++)
		{
			ThreadUtils.run(new Runnable()
			{
				@Override
				public void run()
				{
					long startTime = System.currentTimeMillis();
					GameLog.info("start!");
					for (int i = 0; i < 30000; i++)
					{
						long id = worker2.nextId();
						Long old = keys.putIfAbsent(id, id);
						if (old != null)
						{
							GameLog.error("冲突id=" + id);
						}
					}

					long dt = System.currentTimeMillis() - startTime;
					GameLog.info("ok! " + keys.size() + " dt=" + dt);
				}
			});
		}
	}

	public static void main(String[] args) throws Exception
	{
		testUniqueId();
		System.out.println("LZGLZG StartTime: " + System.currentTimeMillis());
		System.out.println("LZGLZG workerIdBits: " + workerIdBits);
		System.out.println("LZGLZG maxWorkerId: " + maxWorkerId);
		System.out.println("LZGLZG sequenceBits: " + sequenceBits);
		System.out.println("LZGLZG workerIdShift: " + workerIdShift);
		System.out.println("LZGLZG timestampLeftShift: " + timestampLeftShift);
		System.out.println("LZGLZG sequenceMask: " + sequenceMask);
	}
}