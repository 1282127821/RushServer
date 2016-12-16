package com;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSON;
import com.util.GameLog;
import com.util.IDWorker;

public abstract class BaseServer
{
	protected static String configPath;
	private boolean terminate = true;
	public static ServerCfgInfo serverCfgInfo = null;
	public static IDWorker IDWORK = new IDWorker(1);

	public boolean start()
	{
		if (!initComponent(initServerCfg(configPath), "加载服务器配置文件"))
		{
			return false;
		}

		PropertyConfigurator.configure(serverCfgInfo.gameServer.logpath);

		return true;
	}

	public static boolean initServerCfg(String filaPathName)
	{
		try
		{
			FileInputStream fs = new FileInputStream(filaPathName);
			InputStreamReader isr = new InputStreamReader(fs, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder serverJson = new StringBuilder();
			String jsonStr = null;
			while ((jsonStr = reader.readLine()) != null)
			{
				serverJson.append(jsonStr);
			}

			serverCfgInfo = JSON.parseObject(serverJson.toString(), ServerCfgInfo.class);
			reader.close();
			fs.close();
			return true;
		}
		catch (Exception e)
		{
			GameLog.info("加载游戏服务器配置出错" + e.getMessage());
			return false;
		}
	}

	/**
	 * 根据玩家Id获得对应的网关Id
	 */
	public static int getGateWayId(long accountId)
	{
		return (int) (accountId % serverCfgInfo.gateway.size());
	}

	/***
	 * 得到网关的大小
	 */
	public static int getGateSize()
	{
		return serverCfgInfo.gateway.size();
	}

	public boolean isTerminate()
	{
		return terminate;
	}

	public void setTerminate(boolean terminate)
	{
		this.terminate = terminate;
	}

	/**
	 * 初始化相关模块
	 */
	public boolean initComponent(boolean initResult, String componentName)
	{
		if (!initResult)
		{
			GameLog.error(componentName + "错误");
		}
		else
		{
			GameLog.info(componentName + "加载完成");
		}
		return initResult;
	}
}
