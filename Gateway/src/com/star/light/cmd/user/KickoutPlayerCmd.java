package com.star.light.cmd.user;

import org.apache.mina.core.session.IoSession;

import com.star.light.socket.PBMessage;
import com.star.light.user.User;
import com.star.light.user.NetCmd;
import com.star.light.user.UserMgr;

public class KickoutPlayerCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if(user != null){			
			UserMgr.removeOnline(userId, user.getSession());
		}
	}
}
