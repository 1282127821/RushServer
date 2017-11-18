package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamDisbanAction extends AbstractAction
{
	private GamePlayer player;

	public TeamDisbanAction(GamePlayer player)
	{
		this.player = player;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().disbanTeam(player);
	}
}