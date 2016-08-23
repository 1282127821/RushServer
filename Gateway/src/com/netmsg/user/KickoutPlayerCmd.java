package com.netmsg.user;

import com.netmsg.PBMessage;
import com.user.NetCmd;
import com.user.User;
import com.user.UserMgr;

import io.netty.channel.Channel;

public class KickoutPlayerCmd implements NetCmd {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if(user != null){			
			UserMgr.removeOnline(userId, user.getChannel());
		}
	}
}
