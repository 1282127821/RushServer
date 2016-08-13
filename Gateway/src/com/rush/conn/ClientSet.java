package com.rush.conn;

import com.rush.BaseServer;
import com.rush.ServerInfo;
import com.rush.mina.LinkedClient;
import com.rush.protocol.Protocol;
import com.rush.route.ServerRouteHandler;
import com.rush.socket.MessageUtil;
import com.rush.socket.PBMessage;
import com.rush.util.ServerType;

import rush.pbmessage.GamePBMsg.LoadMsg;

/**
 * 该类主要提供给网关服务器使用,管理到其他服务器(castle)的网络连接
 */
public final class ClientSet {
	private static LinkedClient gateWay;
	private static LinkedClient gameServer;

	/**
	 * 初始化,负责连接到其他的服务器
	 */
	public static boolean init() {
		initGateWay();
		connectGameServer();
		return true;
	}

	private static void initGateWay() {
		ServerInfo serverInfo = BaseServer.serverCfgInfo.gateway.get(0);
		gateWay = new LinkedClient(ServerType.GATEWAY, serverInfo.ip, serverInfo.port, null);
	}

	private static void connectGameServer() {
		ServerInfo serverInfo = BaseServer.serverCfgInfo.gameServer;
		ServerRouteHandler routeHandler = new ServerRouteHandler("GameServer");
		gameServer = new LinkedClient(ServerType.GAME, serverInfo.ip, serverInfo.port, routeHandler);
		if (!gameServer.connect()) {
			ClientTryer.getInstance().ctry(gameServer, 10, -1);
		} else {
			sendRegisterMsg(ServerType.GAME, gameServer);
		}
	}

	/**
	 * 删除连接
	 */
	public static void removeServerClient(LinkedClient client) {
		gameServer = null;
	}

	/**
	 * 发送服务器注册消息
	 */
	public static void sendRegisterMsg(int serverType, LinkedClient client) {
		LoadMsg.Builder netMsg = LoadMsg.newBuilder();
		netMsg.setAddress(gateWay.getAddress() == null ? "" : gateWay.getAddress());
		netMsg.setPort(gateWay.getPort());
		netMsg.setType(gateWay.getType());
		netMsg.setConnTimes(gateWay.getConnTimes());
		client.send(MessageUtil.buildMessage(Protocol.C_REGISTER, netMsg));
	}

	/**
	 * 转发数据包到GameServer服务器
	 */
	public static void routeServer(PBMessage packet) {
		if (gameServer != null) {
			gameServer.send(packet);
		}
	}
}
