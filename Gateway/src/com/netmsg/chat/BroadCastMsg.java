package com.netmsg.chat;

import org.apache.mina.core.session.IoSession;

import com.netmsg.PBMessage;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.UserMgr;

public class BroadCastMsg implements NetCmd {
	public void execute(IoSession session, PBMessage packet) throws Exception {
		packet.setCodeId(Protocol.S_C_TIP_INFO);
		UserMgr.broadcastMessage(packet);
	}
}
