package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.alibaba.fastjson.JSON;
import com.util.GameLog;
import com.util.ServerType;

/**
 * 停服命令
 **/
public class StopServer
{
	private static ServerCfgInfo initServerCfg(String filaPathName)
	{
		try (BufferedReader bufReader = Files.newBufferedReader(Paths.get(filaPathName)))
		{
			StringBuilder serverJson = new StringBuilder();
			String jsonStr = null;
			while ((jsonStr = bufReader.readLine()) != null)
			{
				serverJson.append(jsonStr);
			}

			return JSON.parseObject(serverJson.toString(), ServerCfgInfo.class);
		}
		catch (IOException e)
		{
			GameLog.info("加载游戏服务器配置出错" + e.getMessage());
			return null;
		}
	}

	public static void main(String[] args)
	{
		if (args.length < 4)
		{
			System.out.println("Aragment is wrong! eg config,serverType,serverId");
			return;
		}

		ServerCfgInfo serverCfgInfo = initServerCfg(args[0]);
		if (serverCfgInfo == null)
		{
			GameLog.error("init server config fialed., server start failed.");
			return;
		}

		// 服务器类型
		ServerInfo serverInfo = null;
		int serverType = Integer.valueOf(args[1]);
		if (serverType == ServerType.GAME)
		{
			serverInfo = serverCfgInfo.gameServer;
		}
		else if (serverType == ServerType.WEB)
		{
			serverInfo = serverCfgInfo.accountServer;
		}
		else if (serverType == ServerType.GATEWAY)
		{
			serverInfo = serverCfgInfo.gatewayServer;
		}

		if (serverInfo == null)
		{
			System.out.println("Can not found xml config info , stop server failed.");
			return;
		}

		if (args.length == 4 && (args[3].equals("stop") || args[3].equals("stopnow")))
		{
			String ip = "127.0.0.1";
			AdminCmdRequestor adminCmdRequestor = AdminCmdRequestor.connect(ip, serverInfo.adminPort);
			if (adminCmdRequestor != null)
			{
				adminCmdRequestor.send(args[3]);
			}
		}
		else
		{
			System.out.println("参数不正确!");
		}
	}
}
