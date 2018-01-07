package com.web;

import javax.servlet.http.HttpServletRequest;

import com.webserver.BaseHandlerServlet;
import com.webserver.WebHandleAnnotation;

@WebHandleAnnotation(cmdName = "/login", desc = "账号登录")
public class AccountLogin extends BaseHandlerServlet
{
	private static final long serialVersionUID = -6147150952468432920L;

	@Override
	public String execute(HttpServletRequest request)
	{
		try
		{
			return "kick success";
		}
		catch (Exception e)
		{
			return "kick failed. " + e.getMessage();
		}
	}
}
