package com.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.util.JsonUtil;
import com.webserver.BaseHandlerServlet;
import com.webserver.WebHandleAnnotation;

@WebHandleAnnotation(cmdName = "/get_serverinfo", desc = "获取服务器的信息")
public class GetServerInfo extends BaseHandlerServlet
{
	private static final long serialVersionUID = -6147150952468432920L;

	@Override
	public String execute(HttpServletRequest request)
	{
		try
		{
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("version", "20170317");
			paramMap.put("hall", "140.143.93.153:9000");
			paramMap.put("appweb", "https://fir.im/cyrk");
			return JsonUtil.toJson(paramMap);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
