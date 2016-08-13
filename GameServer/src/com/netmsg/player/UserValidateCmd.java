package com.netmsg.player;

import com.GatewayLinkMgr;
import com.mina.LinkedClient;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.LoginReqMsg;
import com.pbmessage.GamePBMsg.PlayerCheckMsg;
import com.player.GamePlayer;
import com.player.LoginMgr;
import com.player.WorldMgr;
import com.protocol.Protocol;
import com.util.GameLog;

public class UserValidateCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		LoginReqMsg loginReq = LoginReqMsg.parseFrom(packet.getMsgBody());
		String token = loginReq.getToken();
		long userId = packet.getUserId();
		long accountId = loginReq.getAccountId();
		boolean result = true;
		if (!token.equalsIgnoreCase(LoginMgr.getKey(accountId))) {
			GameLog.error("验证token值不对: " + token + " " + LoginMgr.getKey(accountId) + " " + accountId);
			result = false;
		}

		if (LoginMgr.isLoginWaitOpen() && WorldMgr.getOnlineCount() >= LoginMgr.getOnlineMan()) {
			GameLog.error("服务器人数达至上限，拒绝: " + userId + "连接.... onlineCount : " + WorldMgr.getOnlineCount());
			result = false;
		}

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
