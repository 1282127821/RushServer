package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamCreateAction;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;

public class TeamCreateCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (player.getTeam() != null) {
			return;
		}
		
		TeamMgr.getInstance().enDefaultQueue(new TeamCreateAction(player));
	}
}