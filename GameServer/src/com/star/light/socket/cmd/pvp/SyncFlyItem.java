package com.star.light.socket.cmd.pvp;

import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.room.Room;
import com.star.light.room.RoomBossInfo;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class SyncFlyItem implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) 
		{
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			if (roomBossInfo.bossMasterId == player.getUserId())
			{
				room.sendSyncMsg(0, Protocol.S_C_SYNC_FLY_ITEM, packet);
			}
		}
	}
}