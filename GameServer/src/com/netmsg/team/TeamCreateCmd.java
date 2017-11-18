package com.netmsg.team;

import com.action.team.TeamCreateAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class TeamCreateCmd implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		if (player.getTeam() != null)
		{
			return;
		}

		RoomMgr.getInstance().addAction(new TeamCreateAction(player));
	}
}