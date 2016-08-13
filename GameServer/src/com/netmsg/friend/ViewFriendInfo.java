package com.netmsg.friend;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;

public class ViewFriendInfo implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		player.getFriendMgr().viewTotalFriendInfo();
	}
}
