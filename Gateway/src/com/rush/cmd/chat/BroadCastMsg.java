package com.rush.cmd.chat;

import org.apache.mina.core.session.IoSession;

import com.rush.protocol.Protocol;
import com.rush.socket.PBMessage;
import com.rush.user.NetCmd;
import com.rush.user.UserMgr;

public class BroadCastMsg implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		packet.setCodeId(Protocol.S_C_TIP_INFO);
		UserMgr.broadcastMessage(packet);
	}
}
