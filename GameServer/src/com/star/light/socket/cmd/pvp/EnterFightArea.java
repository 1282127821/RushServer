package com.star.light.socket.cmd.pvp;

import com.star.light.action.room.EnterFightAreaAction;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class EnterFightArea implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			room.enqueue(new EnterFightAreaAction(room, player));
		}
	}
}
