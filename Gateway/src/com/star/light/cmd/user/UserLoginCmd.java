package com.star.light.cmd.user;

import org.apache.mina.core.session.IoSession;

import com.star.light.conn.ClientSet;
import com.star.light.protocol.Protocol;
import com.star.light.socket.MessageUtil;
import com.star.light.socket.PBMessage;
import com.star.light.user.NetCmd;
import com.star.light.user.UserMgr;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.LoginReqMsg;

public class UserLoginCmd implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		LoginReqMsg netMsg = LoginReqMsg.parseFrom(packet.getMsgBody());
		long userId = packet.getUserId();
		String token = netMsg.getToken();
		if (userId == 0) {
			GameLog.error("UserLoginCmd userId is zero." + token);
			return;
		}

		// 保存未经过验证session
		UserMgr.addTempSession(userId, token, session);
		// 发送验证信息到castle
		LoginReqMsg.Builder verifyMsg = LoginReqMsg.newBuilder();
		verifyMsg.setToken(token);
		verifyMsg.setAccountId(netMsg.getAccountId());
		ClientSet.routeServer(MessageUtil.buildMessage(Protocol.C_S_PLAYER_KEY, userId, verifyMsg.build()));
	}
}
