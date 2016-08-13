package com.netmsg.pvp;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;

public class PVPRandomItem implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		player.pvpRandomItem();
	}
}
