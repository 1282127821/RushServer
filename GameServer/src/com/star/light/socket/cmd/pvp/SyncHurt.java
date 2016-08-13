package com.star.light.socket.cmd.pvp;

import com.star.light.action.room.SyncHurtAction;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;
import com.star.light.room.RoomBossInfo;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.SyncHurtMsg;

public class SyncHurt implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			SyncHurtMsg netMsg = SyncHurtMsg.parseFrom(packet.getMsgBody());
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			int pvpId = netMsg.getPvpId();
			int bossPVPId = roomBossInfo.pvpId;
			if (bossPVPId == pvpId || netMsg.getModelName().contains("boss_")) {
				if (roomBossInfo.bossMasterId == player.getUserId()) {
					room.enqueue(new SyncHurtAction(room, netMsg));
				}
			} else {
				room.enqueue(new SyncHurtAction(room, netMsg));
			}
		}
	}
}