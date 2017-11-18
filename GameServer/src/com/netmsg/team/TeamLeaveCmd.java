package com.netmsg.team;

import com.action.team.TeamLeaveAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.Team;

public class TeamLeaveCmd implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		// 队伍不存在或者队伍不是出于等待中
		Team team = player.getTeam();
		if (team == null || team.getTeamState() != Team.TeamState.ts_wait)
		{
			return;
		}

		RoomMgr.getInstance().addAction(new TeamLeaveAction(player));
	}
}
