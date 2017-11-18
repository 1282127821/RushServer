package com.netmsg.pvp;

import com.action.room.SyncHurtAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SyncHurtMsg;
import com.player.GamePlayer;
import com.room.Room;
import com.room.RoomBossInfo;
import com.room.RoomMgr;

public class SyncHurt implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		Room room = player.getRoom();
		if (room != null)
		{
			SyncHurtMsg netMsg = SyncHurtMsg.parseFrom(packet.getMsgBody());
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			int pvpId = netMsg.getPvpId();
			int bossPVPId = roomBossInfo.pvpId;
			if (bossPVPId == pvpId || netMsg.getModelName().contains("boss_"))
			{
				if (roomBossInfo.bossMasterId == player.getUserId())
				{
					RoomMgr.getInstance().addAction(new SyncHurtAction(room, netMsg));
				}
			}
			else
			{
				RoomMgr.getInstance().addAction(new SyncHurtAction(room, netMsg));
			}
		}
	}
}