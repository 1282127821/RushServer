package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamLeaveAction extends AbstractAction
{
	private GamePlayer player;

	public TeamLeaveAction(GamePlayer player)
	{
		this.player = player;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().leaveTeam(player);
	}
}
