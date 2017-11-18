package com.netmsg.team;

import com.action.team.TeamDisbanAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class TeamDisbanCmd implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		// 玩家是否有队伍了
		if (player.getTeam() != null)
		{
			RoomMgr.getInstance().addAction(new TeamDisbanAction(player));
		}
	}
}
