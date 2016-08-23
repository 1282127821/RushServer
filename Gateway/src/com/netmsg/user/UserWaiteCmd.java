package com.netmsg.user;

import org.apache.mina.core.session.IoSession;

import com.netmsg.MessageUtil;
import com.netmsg.PBMessage;
import com.network.LinkedClient;
import com.pbmessage.GamePBMsg.KickOutPlayerMsg;
import com.pbmessage.GamePBMsg.PlayerCheckMsg;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.User;
import com.user.UserMgr;
import com.util.GameLog;

import io.netty.channel.Channel;

public class UserWaiteCmd implements NetCmd {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		PlayerCheckMsg netMsg = PlayerCheckMsg.parseFrom(packet.getMsgBody());
		long userId = packet.getUserId();
		long accountId = netMsg.getAccountId();
		// 找不到玩家session
		Channel tempChannel = UserMgr.removeTempChannel(userId, netMsg.getToken());
		if (tempChannel == null) {
			GameLog.error("can't find user session. accountId: " + accountId + ",userId:" + userId);
			return;
		}

		// key值验证错误
		if (!netMsg.getResult()) {
			tempChannel.close();
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
				UserMgr.removeOnline(user, user.getChannel());
			}
		}
		UserMgr.addOnline(accountId, userId, tempChannel);
	}
}
