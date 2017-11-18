package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamCombineAction extends AbstractAction
{
	private GamePlayer player;
	private long userId;

	public TeamCombineAction(GamePlayer player, long userId)
	{
		this.player = player;
		this.userId = userId;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().combineTeam(player, userId);
	}
}
