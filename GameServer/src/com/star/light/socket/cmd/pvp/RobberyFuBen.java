package com.star.light.socket.cmd.pvp;

import com.star.light.action.room.RobberyFuBenAction;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.RoomInfoMsg;

public class RobberyFuBen implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		RoomMgr.getInstance().enDefaultQueue(new RobberyFuBenAction(player, RoomInfoMsg.parseFrom(packet.getMsgBody())));
	}
}
