package com;

import com.google.protobuf.AbstractMessage.Builder;
import com.netmsg.PBMessage;
import com.network.LinkedClient;
import com.pbmessage.GamePBMsg.LoadMsg;
import com.util.Log;
import com.util.ServerType;

import io.netty.channel.Channel;

/**
 * 管理网关服务器的连接
 */
public final class GatewayLinkMgr
{
	private LinkedClient gateWay;

	private static GatewayLinkMgr instance = new GatewayLinkMgr();

	public static GatewayLinkMgr getInstance()
	{
		return instance;
	}

	/**
	 * GateWayServer连接到GameServer
	 */
	public void addGameLinkedClient(Channel channel, PBMessage packet) throws Exception
	{
		LoadMsg msg = LoadMsg.parseFrom(packet.getMsgBody());
		LinkedClient client = new LinkedClient(ServerType.GATEWAY, msg.getAddress(), msg.getPort());
		client.setChannel(channel);
		// TODO:LZGLZG以后有多个网关之后需要重新修改
		// session.setAttribute(LinkedClient.KEY_CLIENT, client);
		Log.info("GateWayServer连接到GameServer, addLinkedClient: " + client.toString());
		gateWay = client;
	}

	/**
	 * 根据账号Id获取对应的网关连接
	 */
	public LinkedClient getLinkedClinet(long accountId)
	{
		return gateWay;
	}

	/**
	 * 删除指定的连接
	 */
	public void removeLinkedClient(Channel channel)
	{
		// TODO:LZGLZG这里需要修改
		// LinkedClient client = (LinkedClient)
		// session.getAttribute(LinkedClient.KEY_CLIENT);
		// if (client == null) {
		// return;
		// }
		// gateWay = null;
	}

	/**
	 * 发送到所有网关连接
	 */
	public void sendAll(short code, Builder<?> messageBuilder)
	{
		PBMessage packet = new PBMessage(code);
		if (messageBuilder != null)
		{
			// packet.setMessage(messageBuilder.build());
		}
		gateWay.send(packet);
	}
}
