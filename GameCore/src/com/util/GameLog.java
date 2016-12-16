package com.util;

import org.apache.log4j.Logger;

public class GameLog
{
	// TODO:LZGLZG log4j修改为log4j2的版本，需要进行升级
	private static final String thisClassName = GameLog.class.getName();
	private static final String msgSep = "\r\n";
	private static Logger logger = Logger.getLogger("");

	private static String getStackMsg(String msg)
	{
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if (ste == null)
		{
			return "";
		}

		boolean srcFlag = false;
		for (int i = 0; i < ste.length; i++)
		{
			StackTraceElement s = ste[i];
			// 如果上一行堆栈代码是本类的堆栈，则该行代码则为源代码的最原始堆栈。
			if (srcFlag)
			{
				return s == null ? "" : s.toString() + msgSep + msg;
			}
			// 定位本类的堆栈
			if (thisClassName.equals(s.getClassName()))
			{
				srcFlag = true;
				i++;
			}
		}
		return "";
	}

	public static void debug(String msg)
	{
		logger.debug(getStackMsg(msg));
	}

	public static void debug(String msg, Throwable t)
	{
		logger.debug(getStackMsg(msg), t);
	}

	public static void info(String msg)
	{
		logger.info(getStackMsg(msg));
	}

	public static void info(String msg, Throwable t)
	{
		logger.info(getStackMsg(msg), t);
	}

	public static void warn(String msg)
	{
		logger.warn(getStackMsg(msg));
	}

	public static void warn(String msg, Throwable t)
	{
		logger.warn(getStackMsg(msg), t);
	}

	public static void error(String msg)
	{
		logger.error(getStackMsg(msg));
	}

	public static void error(String msg, Throwable t)
	{
		logger.error(getStackMsg(msg), t);
	}

	public static void fatal(String msg)
	{
		logger.fatal(getStackMsg(msg));
	}

	public static void fatal(String msg, Throwable t)
	{
		logger.fatal(getStackMsg(msg), t);
	}

	public static boolean designError(String fileName, int fileRow, String errorMsg)
	{
		GameLog.error(fileName + "配置表出错，第" + (fileRow + 4) + "行，" + errorMsg);
		return false;
	}
}
