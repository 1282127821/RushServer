package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类，一行代码调用
 */
public class Log
{
	// TODO:LZGLZG log4j修改为log4j2的版本，需要进行升级
	private static final Logger logger = LoggerFactory.getLogger("");
	private static final int BASE_STACK_DEPTH = 4;
	private static final String MSG_SEPERATOR = " ";

	public static void debug(String msg)
	{
		debug0(msg, null);
	}

	public static void debug(String format, Object arg)
	{
		debug0(format, null, arg);
	}

	public static void debug(String format, Object arg1, Object arg2)
	{
		debug0(format, null, arg1, arg2);
	}

	public static void debug(String format, Object... arguments)
	{
		debug0(format, null, arguments);
	}

	public static void debug(String msg, Throwable t)
	{
		debug0(msg, t);
	}

	private static void debug0(String msgOrFormat, Throwable t, Object... objects)
	{
		String className = getCaller();
		if (t != null)
		{
			logger.debug(className + MSG_SEPERATOR + msgOrFormat, t);
		}
		else
		{
			logger.debug(className + MSG_SEPERATOR + msgOrFormat, objects);
		}
	}

	public static void info(String msg)
	{
		info0(msg, null);
	}

	public static void info(String format, Object arg)
	{
		info0(format, null, arg);
	}

	public static void info(String format, Object arg1, Object arg2)
	{
		info0(format, null, arg1, arg2);
	}

	public static void info(String format, Object... arguments)
	{
		info0(format, null, arguments);
	}

	public static void info(String msg, Throwable t)
	{
		info0(msg, t);
	}

	private static void info0(String msgOrFormat, Throwable t, Object... objects)
	{
		String className = getCaller();
		if (t != null)
		{
			logger.info(className + MSG_SEPERATOR + msgOrFormat, t);
		}
		else
		{
			logger.info(className + MSG_SEPERATOR + msgOrFormat, objects);
		}
	}

	public static void warn(String msg)
	{
		warn0(msg, null);
	}

	public static void warn(String format, Object arg)
	{
		warn0(format, null, arg);
	}

	public static void warn(String format, Object arg1, Object arg2)
	{
		warn0(format, null, arg1, arg2);
	}

	public static void warn(String format, Object... arguments)
	{
		warn0(format, null, arguments);
	}

	public static void warn(String msg, Throwable t)
	{
		warn0(msg, t);
	}

	private static void warn0(String msgOrFormat, Throwable t, Object... objects)
	{
		String className = getCaller();
		if (t != null)
		{
			logger.warn(className + MSG_SEPERATOR + msgOrFormat, t);
		}
		else
		{
			logger.warn(className + MSG_SEPERATOR + msgOrFormat, objects);
		}
	}

	public static void error(String msg)
	{
		error0(msg, null);
	}

	public static void error(String format, Object arg)
	{
		error0(format, null, arg);
	}

	public static void error(String format, Object arg1, Object arg2)
	{
		error0(format, null, arg1, arg2);
	}

	public static void error(String format, Object... arguments)
	{
		error0(format, null, arguments);
	}

	public static void error(String msg, Throwable t)
	{
		error0(msg, t);
	}

	private static void error0(String msgOrFormat, Throwable t, Object... objects)
	{
		String className = getCaller();
		if (t != null)
		{
			logger.error(className + MSG_SEPERATOR + msgOrFormat, t);
		}
		else
		{
			logger.error(className + MSG_SEPERATOR + msgOrFormat, objects);
		}
	}

	/**
	 * 获得指定层级的堆栈跟踪元素
	 */
	private static String getCaller()
	{
		StackTraceElement[] st = Thread.currentThread().getStackTrace();
		return st[BASE_STACK_DEPTH].toString();
	}
}