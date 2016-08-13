package com.netmsg.pvp;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SyncCreatProjectileMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.Room;
import com.room.RoomBossInfo;

public class SyncCreateProjectile implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) 
		{
			SyncCreatProjectileMsg netMsg = SyncCreatProjectileMsg.parseFrom(packet.getMsgBody());
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			if (roomBossInfo.pvpId == netMsg.getPvpId()) 
			{
				if (roomBossInfo.bossMasterId == player.getUserId())
				{
					room.sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_CREATEPROJECTILE, packet);
				}
			} 
			else
			{
				room.sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_CREATEPROJECTILE, packet);
			}
		}
	}
}