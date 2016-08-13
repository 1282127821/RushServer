package com.netmsg.pvp;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.protocol.Protocol;

public class SyncAction implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (player.getRoom() != null) {
			player.getRoom().sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_ACTION, packet);
		}
	}
}