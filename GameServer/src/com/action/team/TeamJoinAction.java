package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamJoinAction extends AbstractAction
{
	private long teamId;
	private GamePlayer player;

	public TeamJoinAction(long teamId, GamePlayer player)
	{
		this.teamId = teamId;
		this.player = player;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().joinTeam(this.teamId, player);
	}
}