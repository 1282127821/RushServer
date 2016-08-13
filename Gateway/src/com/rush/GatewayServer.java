package com.rush;

import com.rush.BaseServer;
import com.rush.conn.ConnMgr;
import com.rush.protocol.Protocol;
import com.rush.socket.MessageUtil;
import com.rush.user.NetMsgMgr;
import com.rush.user.UserMgr;
import com.rush.util.GameLog;

import rush.pbmessage.GamePBMsg.KickOutPlayerMsg;

public class GatewayServer extends BaseServer {
	private ConnMgr connMgr;
	private static GatewayServer gatewayServer = new GatewayServer();

	public static GatewayServer getInstance() {
		return gatewayServer;
	}

	public ConnMgr getConnMgr() {
		return connMgr;
	}

	@Override
	public boolean start() {
		if (!super.start()) {
			return false;
		}
		
		if (!initComponent(NetMsgMgr.getInstance().init(), "网络消息协议")) {
			return false;
		}
		
		connMgr = new ConnMgr();
		if (!connMgr.init()) {
			return false;
		}

		// 设置状态
		setTerminate(false);
		return true;
	}

	@Override
	public boolean stop() {
		sendAllPlayerStop();
		setTerminate(true);
		connMgr.stop();
		UserMgr.stop();
		System.exit(0);
		return true;
	}

	/**
	 * 发送停服消息
	 */
	private void sendAllPlayerStop() {
		KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
		kickMsg.setKickOutType(3);
		UserMgr.broadcastMessage(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER, kickMsg));
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("请输入配置文件地址路径/网关服务器ID...");
			return;
		}

		configPath = args[0];
		BaseServer gatewayServer = GatewayServer.getInstance();
		if (!gatewayServer.start()) {
			GameLog.error("GateWayServer启动失败!");
			System.exit(1);
		}
		GameLog.info("GatewayServer启动成功!");
	}
}
