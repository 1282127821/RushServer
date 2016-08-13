package com.star.light.action.team;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;
import com.star.light.team.TeamMgr;

public class TeamSynMessageAction extends Action {
	private GamePlayer player;
	private int taskId;
	
	public TeamSynMessageAction(GamePlayer player, int taskId) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
		this.taskId = taskId;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().synTeamMessage(player, taskId);
	}
}
