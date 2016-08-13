package com.rush.cmd.user;

import org.apache.mina.core.session.IoSession;

import com.rush.protocol.Protocol;
import com.rush.socket.MessageUtil;
import com.rush.socket.PBMessage;
import com.rush.user.NetCmd;
import com.rush.user.User;
import com.rush.user.UserMgr;
import com.rush.util.GameLog;
import com.rush.util.TimeUtil;

import rush.pbmessage.GamePBMsg.SyncServerTimeMsg;

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
