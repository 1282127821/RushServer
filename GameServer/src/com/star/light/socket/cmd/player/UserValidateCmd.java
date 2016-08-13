package com.star.light.socket.cmd.player;

import com.star.light.GatewayLinkMgr;
import com.star.light.mina.LinkedClient;
import com.star.light.player.GamePlayer;
import com.star.light.player.LoginMgr;
import com.star.light.player.WorldMgr;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.LoginReqMsg;
import tbgame.pbmessage.GamePBMsg.PlayerCheckMsg;

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
