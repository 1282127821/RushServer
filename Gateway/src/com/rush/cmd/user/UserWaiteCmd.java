package com.rush.cmd.user;

import org.apache.mina.core.session.IoSession;

import com.rush.mina.LinkedClient;
import com.rush.protocol.Protocol;
import com.rush.socket.MessageUtil;
import com.rush.socket.PBMessage;
import com.rush.user.NetCmd;
import com.rush.user.User;
import com.rush.user.UserMgr;
import com.rush.util.GameLog;

import rush.pbmessage.GamePBMsg.KickOutPlayerMsg;
import rush.pbmessage.GamePBMsg.PlayerCheckMsg;

public class UserWaiteCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		PlayerCheckMsg netMsg = PlayerCheckMsg.parseFrom(packet.getMsgBody());
		long userId = packet.getUserId();
		long accountId = netMsg.getAccountId();
		// 找不到玩家session
		IoSession tempSession = UserMgr.removeTempSession(userId, netMsg.getToken());
		if (tempSession == null) {
			GameLog.error("can't find user session. accountId: " + accountId + ",userId:" + userId);
			return;
		}

		// key值验证错误
		if (!netMsg.getResult()) {
			tempSession.closeNow();
			GameLog.error(" key值验证错误  tempSession.close: " + userId);
			return;
		}

		// 从在线列表中挤人
		User user = UserMgr.getOnlineAccount(accountId);
		if (user != null) {
			try {
				KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
				kickMsg.setKickOutType(1);
				user.sendToClient(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER, kickMsg));
			} catch (Exception e) {
				GameLog.error("挤在线玩家错误  accountId : " + accountId, e);
			} finally {
				long clientId = (Long) user.getSession().getAttribute(LinkedClient.KEY_ID);
				UserMgr.removeOnline(clientId, user.getSession());
			}
		}
		UserMgr.addOnline(accountId, userId, tempSession);
	}
}
