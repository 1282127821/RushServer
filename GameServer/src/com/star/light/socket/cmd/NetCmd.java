package com.star.light.socket.cmd;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;

public interface NetCmd  {
	void execute(GamePlayer player, PBMessage packet) throws Exception;
}
