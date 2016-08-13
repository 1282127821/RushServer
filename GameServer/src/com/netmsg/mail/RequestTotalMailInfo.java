package com.netmsg.mail;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;

public class RequestTotalMailInfo implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		player.getMailMgr().sendTotalMail();
	}
}
