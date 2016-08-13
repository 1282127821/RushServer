package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamJoinAction;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.Team;
import com.star.light.team.TeamMgr;

import tbgame.pbmessage.GamePBMsg.TeamJoinMsg_CS;

public class TeamJoinCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		TeamJoinMsg_CS teamJoinMsg = TeamJoinMsg_CS.parseFrom(packet.getMsgBody());
		long teamId = teamJoinMsg.getTeamId();
		// 队伍不存在或者队伍不是出于等待状态中
		Team team = TeamMgr.getInstance().getTeamById(teamId);
		if (team == null || player.getTeam() != null || team.getTeamState() != Team.TeamState.ts_wait)  {
			return;
		}
			
		TeamMgr.getInstance().enDefaultQueue(new TeamJoinAction(teamId, player));
	}
}
