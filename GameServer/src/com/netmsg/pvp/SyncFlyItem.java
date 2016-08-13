package com.netmsg.pvp;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.Room;
import com.room.RoomBossInfo;

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