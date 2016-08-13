package com.netmsg;

import com.netmsg.PBMessage;
import com.player.GamePlayer;

public interface NetCmd  {
	void execute(GamePlayer player, PBMessage packet) throws Exception;
}
