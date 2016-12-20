package com;

import com.conn.ConnMgr;
import com.netmsg.MessageUtil;
import com.pbmessage.GamePBMsg.KickOutPlayerMsg;
import com.protocol.Protocol;
import com.user.NetMsgMgr;
import com.user.UserMgr;
import com.util.GameLog;

public class GatewayServer extends BaseServer
{
	private static GatewayServer gatewayServer = new GatewayServer();

	public static GatewayServer getInstance()
	{
		return gatewayServer;
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

		if (!ConnMgr.getInstance().init())
		{
			return false;
		}

		// 设置状态
		setTerminate(false);
		return true;
	}

	public boolean stop()
	{
		sendAllPlayerStop();
		setTerminate(true);
		ConnMgr.getInstance().stop();
		UserMgr.stop();
		System.exit(0);
		return true;
	}

	/**
	 * 发送停服消息
	 */
	private void sendAllPlayerStop()
	{
		KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
		kickMsg.setKickOutType(3);
		UserMgr.broadcastMessage(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER, kickMsg));
	}

	public static void main(String[] args)
	{
		String configPath = "../Lib/server.json";
		if (args.length > 1)
		{
			configPath = args[0];
		}

		BaseServer gatewayServer = GatewayServer.getInstance();
		if (!gatewayServer.start(configPath))
		{
			GameLog.error("GateWayServer启动失败!");
			System.exit(1);
		}
		GameLog.info("GatewayServer启动成功!");
	}
}
