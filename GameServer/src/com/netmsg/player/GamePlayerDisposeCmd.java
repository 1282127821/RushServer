package com.netmsg.player;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.player.WorldMgr;

/**
 * 卸载玩家数据
 */
public class GamePlayerDisposeCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (!player.isOnline()) {
			WorldMgr.unLoadPlayer(player);
		}
	}
}
