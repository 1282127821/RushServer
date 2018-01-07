package com.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.util.Log;

/**
 * 处理参数（params）为json格式字符串的Servlet基类
 */
public abstract class BaseHandlerServlet extends HttpServlet
{
	private static final long serialVersionUID = -7337936262053988938L;

	/**
	 * 请求客户端的IP
	 */
	private String requestIP = null;

	/**
	 * 非法的客户端IP
	 */
	protected static final String INVALID_IP = "ip is llegal,no permission";

	/**
	 * 用于处理实体类的Gson实例
	 */
	protected final Gson jsonParser = new Gson();

	public BaseHandlerServlet()
	{
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String result = null;
		boolean isOpenGM = true;// GlobalConfigManager.getInstance().getVariableConfig().isOpenGM();
		// IP 检查
		// requestIP = ServletUtil.getRequestIP(request);
		if (!isOpenGM && !checkRequestIP(requestIP))
		{
			result = jsonParser.toJson(INVALID_IP);
		}
		else
		{
			result = execute(request);
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}

	/**
	 * 处理命令接口
	 */
	protected abstract String execute(HttpServletRequest request);

	/**
	 * 检查客户端IP是否合法
	 */
	protected boolean checkRequestIP(String ip)
	{
		// GlobalConfigManager.getInstance().getWebRequestConfig().checkAdmin(ip)
		return true;
	}

	/**
	 * 获取请求客户端的IP
	 */
	protected String getRequstIP()
	{
		return requestIP;
	}

	/**
	 * 转换utf-8编码
	 */
	public String getUTF8(String value)
	{
		try
		{
			if (!checkGBK(value))
			{
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			}
			else
			{
				return value;
			}
		}
		catch (Exception e)
		{
			Log.error("获取字符转换成utf-8出错", e);
		}

		return "";
	}

	/**
	 * 检查能否用GBK解码
	 */
	public boolean checkGBK(String value)
	{
		return Charset.forName("GBK").newEncoder().canEncode(value);
	}
}
