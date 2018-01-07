package com.web;

import javax.servlet.http.HttpServletRequest;

import com.util.Log;
import com.webserver.BaseHandlerServlet;
import com.webserver.WebHandleAnnotation;

@WebHandleAnnotation(cmdName = "/guest", desc = "游客登录")
public class GuestLogin extends BaseHandlerServlet
{
	private static final long serialVersionUID = -6147150952468432920L;

	@Override
	public String execute(HttpServletRequest request)
	{
		try
		{
			return "";
		}
		catch (Exception e)
		{
			Log.error(e.getMessage());
			return "";
		}
	}
}
