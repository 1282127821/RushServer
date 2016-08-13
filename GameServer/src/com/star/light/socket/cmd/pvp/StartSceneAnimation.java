package com.star.light.socket.cmd.pvp;

import com.star.light.action.room.StartSceneAnimationAction;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class StartSceneAnimation implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			room.enqueue(new StartSceneAnimationAction(room));
		}
	}
}
