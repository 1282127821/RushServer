package com.netmsg.team;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class RequestTeamInfo implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		TeamMgr.getInstance().requestTeamInfo(player);
	}
}
