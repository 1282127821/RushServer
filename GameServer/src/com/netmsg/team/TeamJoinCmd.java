package com.netmsg.team;

import com.action.team.TeamJoinAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.TeamJoinMsg_CS;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.Team;
import com.team.TeamMgr;

public class TeamJoinCmd implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		TeamJoinMsg_CS teamJoinMsg = TeamJoinMsg_CS.parseFrom(packet.getMsgBody());
		long teamId = teamJoinMsg.getTeamId();
		// 队伍不存在或者队伍不是出于等待状态中
		Team team = TeamMgr.getInstance().getTeamById(teamId);
		if (team == null || player.getTeam() != null || team.getTeamState() != Team.TeamState.ts_wait)
		{
			return;
		}

		RoomMgr.getInstance().addAction(new TeamJoinAction(teamId, player));
	}
}
