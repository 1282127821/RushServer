package com.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.util.JsonUtil;
import com.util.StringUtil;
import com.webserver.BaseHandlerServlet;
import com.webserver.WebHandleAnnotation;

@WebHandleAnnotation(cmdName = "/base_info", desc = "获取玩家的基本信息")
public class GetUserBaseInfo extends BaseHandlerServlet
{
	private static final long serialVersionUID = -6147150952468432920L;

	@Override
	public String execute(HttpServletRequest request)
	{
		try
		{
			String strUserId = request.getParameter("userid");
			if (StringUtil.isNull(strUserId))
				return "recharge failed " + getClass().getName();

			long userId = Long.valueOf(strUserId);
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("errcode", "0");
			paramMap.put("errmsg", "ok");
			paramMap.put("name", "https://fir.im/cyrk");
			paramMap.put("sex", "https://fir.im/cyrk");
			paramMap.put("headimgurl", "https://fir.im/cyrk");
			return JsonUtil.toJson(paramMap);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
