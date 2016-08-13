package com.rush.cmd.user;
import org.apache.mina.core.session.IoSession;

import com.rush.protocol.Protocol;
import com.rush.socket.MessageUtil;
import com.rush.socket.PBMessage;
import com.rush.user.NetCmd;
import com.rush.user.User;
import com.rush.user.UserMgr;

import rush.pbmessage.GamePBMsg.KickOutPlayerMsg;

public class DeletePlayerCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if (user != null) {
			KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
			kickMsg.setKickOutType(2);
			user.sendToClient(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER, kickMsg));
			UserMgr.removeOnline(userId, user.getSession());
		}
	}
}
