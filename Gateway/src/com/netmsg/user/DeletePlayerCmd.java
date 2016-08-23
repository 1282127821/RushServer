package com.netmsg.user;
import com.netmsg.MessageUtil;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.KickOutPlayerMsg;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.User;
import com.user.UserMgr;

import io.netty.channel.Channel;

public class DeletePlayerCmd implements NetCmd {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if (user != null) {
			KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
			kickMsg.setKickOutType(2);
			user.sendToClient(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER, kickMsg));
			UserMgr.removeOnline(userId, user.getChannel());
		}
	}
}
