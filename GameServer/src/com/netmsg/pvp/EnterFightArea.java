package com.netmsg.pvp;

import com.action.room.EnterFightAreaAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.Room;

public class EnterFightArea implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			room.enqueue(new EnterFightAreaAction(room, player));
		}
	}
}
