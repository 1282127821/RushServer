package com.netmsg.pvp;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.Room;

public class SyncFlyPosition implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) 
		{
			room.sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_FLY_POSITION, packet);
		}
	}
}