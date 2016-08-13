package com.netmsg.pvp;

import com.action.room.StartSceneAnimationAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.Room;

public class StartSceneAnimation implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			room.enqueue(new StartSceneAnimationAction(room));
		}
	}
}
