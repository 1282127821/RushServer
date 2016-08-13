package com.action.team;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.TeamMgr;

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
