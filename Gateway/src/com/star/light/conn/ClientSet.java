package com.star.light.conn;

import com.star.light.BaseServer;
import com.star.light.ServerInfo;
import com.star.light.mina.LinkedClient;
import com.star.light.protocol.Protocol;
import com.star.light.route.ServerRouteHandler;
import com.star.light.socket.MessageUtil;
import com.star.light.socket.PBMessage;
import com.star.light.util.ServerType;

import tbgame.pbmessage.GamePBMsg.LoadMsg;

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
