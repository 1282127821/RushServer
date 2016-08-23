package com.netmsg.chat;

import com.netmsg.PBMessage;
import com.protocol.Protocol;
import com.user.NetCmd;
import com.user.UserMgr;

import io.netty.channel.Channel;

public class BroadCastMsg implements NetCmd {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		packet.setCodeId(Protocol.S_C_TIP_INFO);
		UserMgr.broadcastMessage(packet);
	}
}
