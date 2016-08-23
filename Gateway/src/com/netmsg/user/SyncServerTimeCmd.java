package com.netmsg.user;

import com.netmsg.MessageUtil;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SyncServerTimeMsg;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.User;
import com.user.UserMgr;
import com.util.GameLog;
import com.util.TimeUtil;

import io.netty.channel.Channel;

public class SyncServerTimeCmd implements NetCmd {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		long userId = packet.getUserId();
		User user = UserMgr.getOnlineUser(userId);
		if (user == null) {
			GameLog.error("客户端同步时间, user is null. userId : " + userId);
			return;
		}
		
		SyncServerTimeMsg.Builder netMsg = SyncServerTimeMsg.newBuilder();
		netMsg.setServerTime(TimeUtil.getSysCurSeconds());
		user.sendToClient(MessageUtil.buildMessage(Protocol.S_C_SYNC_SERVER_TIME, netMsg));
	}
}
