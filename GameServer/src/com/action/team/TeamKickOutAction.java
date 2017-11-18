package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamKickOutAction extends AbstractAction
{
	private GamePlayer player;
	private long userId;

	public TeamKickOutAction(GamePlayer player, long userId)
	{
		this.player = player;
		this.userId = userId;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().kickOutTeam(player, userId);
	}
}
