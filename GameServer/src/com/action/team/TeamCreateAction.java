package com.action.team;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.TeamMgr;

public class TeamCreateAction extends Action {
	private GamePlayer player;
	
	public TeamCreateAction(GamePlayer player) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().createTeam( player);
	}
}
