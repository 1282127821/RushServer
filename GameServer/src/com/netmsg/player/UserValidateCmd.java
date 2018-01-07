package com.netmsg.player;

import com.GatewayLinkMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.network.LinkedClient;
import com.pbmessage.GamePBMsg.LoginReqMsg;
import com.pbmessage.GamePBMsg.PlayerCheckMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;

public class UserValidateCmd implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		LoginReqMsg loginReq = LoginReqMsg.parseFrom(packet.getMsgBody());
		String token = loginReq.getToken();
		long accountId = loginReq.getAccountId();
		boolean result = true;
		PlayerCheckMsg.Builder checkMsg = PlayerCheckMsg.newBuilder();
		checkMsg.setResult(result);
		checkMsg.setToken(token);
		checkMsg.setAccountId(accountId);
		// 把网关连接缓存起来，后续发送消息则不再需要每次获取网关连接
		LinkedClient gateWayclient = GatewayLinkMgr.getInstance().getLinkedClinet(accountId);
		player.gateWayclient = gateWayclient;
		player.sendPacket(Protocol.G_PLAYER_WAITE, checkMsg);
	}
}
