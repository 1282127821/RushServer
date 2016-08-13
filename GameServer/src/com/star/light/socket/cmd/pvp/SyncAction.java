package com.star.light.socket.cmd.pvp;

import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class SyncAction implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (player.getRoom() != null) {
			player.getRoom().sendSyncMsg(player.getUserId(), Protocol.S_C_SYNC_ACTION, packet);
		}
	}
}