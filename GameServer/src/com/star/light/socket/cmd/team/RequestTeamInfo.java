package com.star.light.socket.cmd.team;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;

public class RequestTeamInfo implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		TeamMgr.getInstance().requestTeamInfo(player);
	}
}
