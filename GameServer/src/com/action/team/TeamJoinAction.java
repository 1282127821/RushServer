package com.action.team;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.TeamMgr;

public class TeamJoinAction extends Action{
	private long teamId;
	private GamePlayer player;
	
	public TeamJoinAction(long teamId, GamePlayer player) {
		super(RoomMgr.executor.getDefaultQueue());
		this.teamId = teamId;
		this.player = player;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().joinTeam(this.teamId, player);
	}
}