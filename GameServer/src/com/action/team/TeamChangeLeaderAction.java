package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamChangeLeaderAction extends AbstractAction
{
	private GamePlayer player;
	private long userId;

	public TeamChangeLeaderAction(GamePlayer player, long userId)
	{
		this.player = player;
		this.userId = userId;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().changeLeaderTeam(player, userId);
	}
}
