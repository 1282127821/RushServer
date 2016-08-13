package com.star.light.socket.cmd.player;

import com.star.light.player.GamePlayer;
import com.star.light.player.WorldMgr;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

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
