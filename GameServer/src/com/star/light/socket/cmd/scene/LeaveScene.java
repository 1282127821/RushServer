package com.star.light.socket.cmd.scene;

import com.star.light.player.GamePlayer;
import com.star.light.scene.SceneMgr;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.Team;

public class LeaveScene implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		//修改队伍状态
		Team team = player.getTeam();
		if(team != null && team.isLeader(player.getUserId())) {
			team.setTeamState(Team.TeamState.ts_fight);
		}
		
		SceneMgr.getInstance().playerLeave(player);
	}
}
