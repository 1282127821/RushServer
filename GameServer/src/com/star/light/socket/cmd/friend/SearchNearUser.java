package com.star.light.socket.cmd.friend;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class SearchNearUser implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		player.getFriendMgr().searchNearUser();
	}
}