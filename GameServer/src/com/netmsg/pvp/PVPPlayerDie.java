package com.netmsg.pvp;

import java.util.List;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SyncPVPCommonMsg;
import com.player.GamePlayer;
import com.room.Room;
import com.room.RoomBossInfo;
import com.room.RoomPlayer;
import com.util.Log;

public class PVPPlayerDie implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			SyncPVPCommonMsg netMsg = SyncPVPCommonMsg.parseFrom(packet.getMsgBody());
			RoomBossInfo roomBossInfo = room.roomBossInfo;
			if (roomBossInfo.pvpId == netMsg.getPvpId()) {
				// 判断房间里面是否有跟自己敌对阵营的玩家 包括BOSS也需要判断 需要设置房间玩家的状态为死亡
				if (roomBossInfo.bossMasterId == player.getUserId()) {
					Log.info("LZGLZG PVPPlayerDie BOSS Die " + netMsg.getPvpId());
					roomBossInfo.isDead = true;
					room.isPVPEnd();
				}
			} else {
				List<RoomPlayer> roomPlayerList = room.getTotalRoomPlayer();
				for (RoomPlayer roomPlayer : roomPlayerList) {
					if (roomPlayer.pvpId == netMsg.getPvpId()) {
						Log.info("LZGLZG PVPPlayerDie PlayerDie " + netMsg.getPvpId());
						roomPlayer.isDead = true;
						room.isPVPEnd();
						break;
					}
				}
			}
		}
	}
}
