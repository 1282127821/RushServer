package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamDisbanAction;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;

public class TeamDisbanCmd  implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		// 玩家是否有队伍了
		if(player.getTeam() != null) {
			TeamMgr.getInstance().enDefaultQueue(new TeamDisbanAction(player));
		}
	}
}
