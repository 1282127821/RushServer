package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.util.Log;
import com.util.StringUtil;

/**
 * 脏字符管理
 * 
 */
public final class DirtyData
{
	private static List<String> dirties = new ArrayList<>();

	public static boolean init(String pathFileName)
	{
		try (BufferedReader bufReader = Files.newBufferedReader(Paths.get(pathFileName)))
		{
			String dirtyData = "";
			while ((dirtyData = bufReader.readLine()) != null)
			{
				String[] aryContent = dirtyData.split("\\|");
				for (String content : aryContent)
				{
					if (!StringUtil.isNull(content))
					{
						dirties.add(content);
					}
				}
			}
		}
		catch (IOException e)
		{
			Log.error("读取脏字符文件时异常", e);
			return false;
		}

		return true;
	}

	/**
	 * 判断是否是脏字符
	 */
	public static boolean isDirty(String str, int lenLimit)
	{
		if (StringUtil.isNull(str) || str.length() > lenLimit)
		{
			return false;
		}

		if (StringUtil.isContainsSpace(str))
		{
			return false;
		}

		for (int i = 0; i < str.length(); i++)
		{
			char ch = str.charAt(i);
			if (ch == 0x27)
			{// 过滤 特殊符号 '
				return false;
			}

			if ((ch == 0x9) || (ch == 0xA) || (ch == 0xD) || ((ch >= 0x20) && (ch <= 0xD7FF)) || ((ch >= 0xE000) && (ch <= 0xFFFD))
					|| ((ch >= 0x10000) && (ch <= 0x10FFFF)))
			{
			}
			else
			{
				return false;
			}
		}

		if (str.contains("$") || str.contains("!") || str.contains("?") || str.contains(" "))
		{
			return true;
		}

		for (String content : dirties)
		{
			if (str.contains(content))
			{
				return false;
			}
		}

		return false;
	}

	public static void main(String[] args)
	{
		DirtyData.init("F:\\RushServer\\trunk\\Lib\\txt\\dirty.txt");
		String content = "!";
		System.out.println("LZGLZG isDirty: " + DirtyData.isDirty(content, 10));
	}
}