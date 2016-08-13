package com.action.team;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.TeamMgr;

public class TeamCombineAction extends Action {
	private GamePlayer player;
	private long userId;
	
	public TeamCombineAction(GamePlayer player, long userId) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
		this.userId = userId;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().combineTeam(player, userId);
	}
}
