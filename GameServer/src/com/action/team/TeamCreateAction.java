package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamCreateAction extends AbstractAction
{
	private GamePlayer player;

	public TeamCreateAction(GamePlayer player)
	{
		this.player = player;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().createTeam(player);
	}
}
