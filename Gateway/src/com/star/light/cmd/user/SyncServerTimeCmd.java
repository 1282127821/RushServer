package com.star.light.cmd.user;

import org.apache.mina.core.session.IoSession;

import com.star.light.protocol.Protocol;
import com.star.light.socket.MessageUtil;
import com.star.light.socket.PBMessage;
import com.star.light.user.User;
import com.star.light.user.NetCmd;
import com.star.light.user.UserMgr;
import com.star.light.util.GameLog;
import com.star.light.util.TimeUtil;

import tbgame.pbmessage.GamePBMsg.SyncServerTimeMsg;

public class SyncServerTimeCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
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
