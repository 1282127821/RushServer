package com.util;

import java.util.regex.Pattern;

public final class StringUtil
{
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String SEMICOLON = ";";
	public static final String ELEMENT_SPLIT = "\\|";
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	public static final long[] EMPTY_LONG_ARRAY = new long[0];
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

	public static boolean isNumeric(String str)
	{
		return Pattern.compile("[0-9]*").matcher(str).matches();
	}

	public static int[] splitToInt(String str)
	{
		return splitToInt(str, ",");
	}

	public static int[] splitToInt(String str, String spStr)
	{
		if (str.trim().length() == 0)
		{
			return EMPTY_INT_ARRAY;
		}

		String[] temps = str.split(spStr);
		int len = temps.length;
		int[] results = new int[len];
		for (int i = 0; i < len; i++)
		{
			results[i] = Integer.parseInt(temps[i].trim());
		}
		return results;
	}

	public static long[] splitToLong(String str)
	{
		return splitToLong(str, ",");
	}

	public static long[] splitToLong(String str, String spStr)
	{
		if (str.trim().length() == 0)
		{
			return EMPTY_LONG_ARRAY;
		}

		String[] temps = str.split(spStr);
		int len = temps.length;
		long[] results = new long[len];
		for (int i = 0; i < len; i++)
		{
			results[i] = Long.parseLong(temps[i].trim());
		}
		return results;
	}

	public static double[] splitToDouble(String str)
	{
		return splitToDouble(str, ",");
	}

	public static double[] splitToDouble(String str, String spStr)
	{
		if (str.trim().length() == 0)
		{
			return EMPTY_DOUBLE_ARRAY;
		}

		String[] temps = str.split(spStr);
		int len = temps.length;
		double[] results = new double[len];
		for (int i = 0; i < len; i++)
		{
			results[i] = Double.parseDouble(temps[i].trim());
		}
		return results;
	}

	/**
	 * 判断字符串是否为 null 或者 空串
	 */
	public static boolean isNull(String content)
	{
		return content == null || content.trim().length() == 0;
	}

	/**
	 * 判断给定的字符为不为null和空串
	 */
	public static boolean isNotNull(String content)
	{
		return !isNull(content);
	}

	/**
	 * 通过ascii做非空判断
	 */
	public static boolean isContainsSpace(String str)
	{
		if (str == null)
		{
			return true;
		}

		char[] b = str.toCharArray();
		for (int i = 0; i < b.length; i++)
		{
			if ((int) b[i] == 32)
			{
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args)
	{

	}
}
