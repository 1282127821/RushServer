package com.webserver;

import java.util.List;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.WebServerConfig;
import com.util.ClassUtil;
import com.util.Log;

/**
 * 嵌入式http调用使用的连接组件
 */
public class WebServerMgr
{
	/**
	 * jetty自带的server
	 */
	private static Server server;

	/**
	 * 触发的处理器
	 */
	private static ServletContextHandler context;

	/**
	 * 处理器列表
	 */
	private static HandlerList handlerList = new HandlerList();

	/**
	 * 触发的资源处理器
	 */
	private static ResourceHandler resourceHandler;

	public static boolean init(WebServerConfig webServerConfig)
	{
		server = new Server(webServerConfig.port);
		try
		{
			context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			context.setResourceBase("");
			resourceHandler = new ResourceHandler();
			resourceHandler.setResourceBase(webServerConfig.resourcePath);
			handlerList.addHandler(context);
			handlerList.addHandler(resourceHandler);
			server.setHandler(handlerList);
			loadWebServlet(webServerConfig.packages);
			server.start();
		}
		catch (Exception e)
		{
			Log.error("WebServerMgr init error", e);
			return false;
		}

		return true;
	}

	/**
	 * 使用WebServerConfig加载Servlet类
	 */
	private static boolean loadWebServlet(String packageName)
	{
		List<Class<?>> activityClass = ClassUtil.getClasses(packageName);
		for (Class<?> servletClass : activityClass)
		{
			WebHandleAnnotation annotation = servletClass.getAnnotation(WebHandleAnnotation.class);
			if (annotation != null)
			{
				try
				{
					Servlet servlet = (Servlet) servletClass.newInstance();
					context.addServlet(new ServletHolder(servlet), annotation.cmdName());
				}
				catch (Exception e)
				{
					Log.error("loadWebServlet error", e);
					continue;
				}
			}
		}

		return true;
	}

	public static void stop()
	{
		try
		{
			server.stop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
