package com.netmsg.user;

import org.apache.mina.core.session.IoSession;

import com.netmsg.PBMessage;
import com.user.NetCmd;
import com.user.User;
import com.user.UserMgr;

public class KickoutPlayerCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if(user != null){			
			UserMgr.removeOnline(userId, user.getSession());
		}
	}
}
