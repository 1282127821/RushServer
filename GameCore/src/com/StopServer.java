package com;

import com.util.GameLog;
import com.util.ServerType;

/**
 * 停服命令
 **/
public class StopServer {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Aragment is wrong! eg config,serverType,serverId");
			return;
		}

		// 初始化配置文件
		if (!BaseServer.initServerCfg(args[0])) {
			GameLog.error("init server config fialed., server start failed.");
			return;
		}

		// 服务器类型
		ServerInfo serverInfo = null;
		int serverType = Integer.valueOf(args[1]);
		if (serverType == ServerType.GAME) {
			serverInfo = BaseServer.serverCfgInfo.gameServer;
		} else if (serverType == ServerType.WEB) {
			serverInfo = BaseServer.serverCfgInfo.accountServer;
		} else if (serverType == ServerType.GATEWAY) {
			serverInfo = BaseServer.serverCfgInfo.gateway.get(0);
		}
		
		if (serverInfo == null) {
			System.out.println("Can not found xml config info , stop server failed.");
			return;
		}

		if (args.length == 4 && (args[3].equals("stop") || args[3].equals("stopnow"))) {
			String ip = "127.0.0.1";
			AdminCmdRequestor adminCmdRequestor = AdminCmdRequestor.connect(ip, serverInfo.adminPort);
			if (adminCmdRequestor != null) {
				adminCmdRequestor.send(args[3]);
			}
		} else {
			System.out.println("参数不正确!");
		}
	}
}
