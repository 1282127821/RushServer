package com.util;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

public class JsonUtil
{
	/**
	 * 将参数的map转换成json格式的字符串
	 */
	public static String toJson(HashMap<String, String> paramMap) throws IOException
	{
		Gson jsonParser = new Gson();
		return jsonParser.toJson(paramMap);
	}
}
