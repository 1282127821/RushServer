package com.star.light.socket.cmd.pvp;

import com.star.light.action.room.AddShieldHPAction;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.SyncPVPCommonMsg;

public class SyncShieldHP implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) 
		{
			SyncPVPCommonMsg netMsg = SyncPVPCommonMsg.parseFrom(packet.getMsgBody());
			room.enqueue(new AddShieldHPAction(room, player, netMsg.getOperType()));
		}
	}
}