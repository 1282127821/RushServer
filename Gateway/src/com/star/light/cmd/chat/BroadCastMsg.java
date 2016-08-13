package com.star.light.cmd.chat;

import org.apache.mina.core.session.IoSession;

import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.user.NetCmd;
import com.star.light.user.UserMgr;

public class BroadCastMsg implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		packet.setCodeId(Protocol.S_C_TIP_INFO);
		UserMgr.broadcastMessage(packet);
	}
}
