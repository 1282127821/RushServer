package com.netmsg.user;

import com.conn.ClientSet;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.LoginReqMsg;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.UserMgr;
import com.util.GameLog;

import io.netty.channel.Channel;

public class UserLoginCmd implements NetCmd {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		LoginReqMsg netMsg = LoginReqMsg.parseFrom(packet.getMsgBody());
		long userId = packet.getUserId();
		String token = netMsg.getToken();
		if (userId == 0) {
			GameLog.error("UserLoginCmd userId is zero." + token);
			return;
		}

		// 保存未经过验证session
		UserMgr.addTempChannel(userId, token, channel);
		// 发送验证信息到castle
		LoginReqMsg.Builder verifyMsg = LoginReqMsg.newBuilder();
		verifyMsg.setToken(token);
		verifyMsg.setAccountId(netMsg.getAccountId());
		
		PBMessage keyMsg = new PBMessage(Protocol.C_S_PLAYER_KEY, userId);
		keyMsg.setMessage(verifyMsg.build());
		ClientSet.routeServer(keyMsg);
	}
}
