package com;

import com.db.DBPoolMgr;
import com.game.NetMsgMgr;
import com.game.NettyMgr;
import com.game.ServerChannelInitializer;
import com.game.TimerTaskMgr;
import com.util.Log;
import com.webserver.WebServerMgr;

public class LoginServer extends BaseServer
{
	private static LoginServer loginServer = new LoginServer();

	public static LoginServer getInstance()
	{
		return loginServer;
	}

	@Override
	public boolean start(String configPath)
	{
		if (!super.start(configPath))
		{
			return false;
		}

		if (!initComponent(NetMsgMgr.getInstance().init(), "网络消息协议"))
		{
			return false;
		}

		// 监听客户端连接
		if (!initComponent(NettyMgr.start("10.10.11.20", 5010, new ServerChannelInitializer(new LoginServerHandler())), "Netty service"))
		{
			return false;
		}

		WebServerConfig webServerConfig = new WebServerConfig();
		webServerConfig.port = 6002;
		webServerConfig.resourcePath = "../Lib/config";
		webServerConfig.packages = "com.web";
		if (!initComponent(WebServerMgr.init(webServerConfig), "WebServerMgr"))
			return false;

		DBPoolMgr.getInstance().initDBPool(serverCfgInfo.mainDb);
		TimerTaskMgr.init();
		return true;
	}

	public boolean stop()
	{
		setTerminate(true);
		NettyMgr.stop();
		DBPoolMgr.getInstance().closeDBPool();
		System.exit(0);
		return true;
	}

	public static void main(String[] args)
	{
		String configPath = "../Lib/server.json";
		if (args.length > 1)
		{
			configPath = args[0];
		}

		BaseServer loginServer = LoginServer.getInstance();
		if (!loginServer.start(configPath))
		{
			Log.error("LoginServer启动失败!");
			System.exit(1);
		}
		Log.info("LoginServer启动成功!");
	}
}
