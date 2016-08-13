package com.netmsg.scene;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.scene.SceneMgr;
import com.team.Team;

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
