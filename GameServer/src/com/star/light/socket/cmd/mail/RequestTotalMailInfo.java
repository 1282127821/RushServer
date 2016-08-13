package com.star.light.socket.cmd.mail;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class RequestTotalMailInfo implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		player.getMailMgr().sendTotalMail();
	}
}
