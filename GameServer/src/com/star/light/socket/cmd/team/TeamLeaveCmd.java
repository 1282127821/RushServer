package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamLeaveAction;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.Team;
import com.star.light.team.TeamMgr;

public class TeamLeaveCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		// 队伍不存在或者队伍不是出于等待中
		Team team = player.getTeam();
		if (team == null || team.getTeamState() != Team.TeamState.ts_wait) {
			return;
		}
		
		TeamMgr.getInstance().enDefaultQueue(new TeamLeaveAction(player));
	}
}
