package com.action.team;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.TeamMgr;

public class TeamDisbanAction extends Action{
	private GamePlayer player;
	
	public TeamDisbanAction(GamePlayer player) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().disbanTeam(player);
	}
}