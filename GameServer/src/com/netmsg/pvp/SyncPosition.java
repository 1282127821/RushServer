package com.netmsg.pvp;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SyncPositionMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.Room;
import com.room.RoomBossInfo;

public class SyncPosition implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null)
		{
			SyncPositionMsg netMsg = SyncPositionMsg.parseFrom(packet.getMsgBody());
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			if (roomBossInfo.pvpId == netMsg.getPvpId()) 
			{
				if (roomBossInfo.bossMasterId == player.getUserId())
				{
					room.sendSyncMsg(0, Protocol.S_C_SYNC_POSITION, packet);
				}
			} 
			else
			{
				room.sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_POSITION, packet);
			}
		}
	}
}