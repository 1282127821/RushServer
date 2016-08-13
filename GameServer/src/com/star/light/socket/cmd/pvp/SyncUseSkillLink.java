package com.star.light.socket.cmd.pvp;

import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.room.Room;
import com.star.light.room.RoomBossInfo;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.SyncUseSkillLinkMsg;

public class SyncUseSkillLink implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) 
		{
			SyncUseSkillLinkMsg netMsg = SyncUseSkillLinkMsg.parseFrom(packet.getMsgBody());
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			if (roomBossInfo.pvpId == netMsg.getPvpId()) 
			{
				if (roomBossInfo.bossMasterId == player.getUserId())
				{
					room.sendSyncMsg(0, Protocol.S_C_SYNC_USE_SKILL_LINK, packet);
				}
			} 
			else
			{
				room.sendSyncMsg(0, Protocol.S_C_SYNC_USE_SKILL_LINK, packet);
			}
		}
	}
}