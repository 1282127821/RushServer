package com.rush.cmd.user;

import org.apache.mina.core.session.IoSession;

import com.rush.socket.PBMessage;
import com.rush.user.NetCmd;
import com.rush.user.User;
import com.rush.user.UserMgr;

public class KickoutPlayerCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if(user != null){			
			UserMgr.removeOnline(userId, user.getSession());
		}
	}
}
