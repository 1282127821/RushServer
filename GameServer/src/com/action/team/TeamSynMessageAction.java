package com.action.team;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamSynMessageAction extends AbstractAction
{
	private GamePlayer player;
	private int taskId;

	public TeamSynMessageAction(GamePlayer player, int taskId)
	{
		this.player = player;
		this.taskId = taskId;
	}

	@Override
	public void execute()
	{
		TeamMgr.getInstance().synTeamMessage(player, taskId);
	}
}
