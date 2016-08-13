package com.netmsg.user;
import org.apache.mina.core.session.IoSession;

import com.netmsg.MessageUtil;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.KickOutPlayerMsg;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.User;
import com.user.UserMgr;

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
