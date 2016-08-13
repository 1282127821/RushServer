package com.star.light.socket.cmd.chat;

import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.BlackInfoMsg;

public class DeleteBlackUser implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		BlackInfoMsg netMsg = BlackInfoMsg.parseFrom(packet.getMsgBody());
		player.deleteBlackUser(netMsg.getUserName());
		packet.setCodeId(Protocol.S_C_DELETE_BLACK_USER);
		player.sendPacket(packet);
	}
}
