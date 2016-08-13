package com.netmsg.chat;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.BlackInfoMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;

public class DeleteBlackUser implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		BlackInfoMsg netMsg = BlackInfoMsg.parseFrom(packet.getMsgBody());
		player.deleteBlackUser(netMsg.getUserName());
		packet.setCodeId(Protocol.S_C_DELETE_BLACK_USER);
		player.sendPacket(packet);
	}
}
