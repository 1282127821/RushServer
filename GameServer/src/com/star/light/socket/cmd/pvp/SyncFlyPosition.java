package com.star.light.socket.cmd.pvp;

import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.room.Room;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class SyncFlyPosition implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) 
		{
			room.sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_FLY_POSITION, packet);
		}
	}
}