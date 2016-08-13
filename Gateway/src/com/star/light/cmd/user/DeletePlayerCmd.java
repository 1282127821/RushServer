package com.star.light.cmd.user;
import org.apache.mina.core.session.IoSession;

import com.star.light.protocol.Protocol;
import com.star.light.socket.MessageUtil;
import com.star.light.socket.PBMessage;
import com.star.light.user.User;
import com.star.light.user.NetCmd;
import com.star.light.user.UserMgr;

import tbgame.pbmessage.GamePBMsg.KickOutPlayerMsg;

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
