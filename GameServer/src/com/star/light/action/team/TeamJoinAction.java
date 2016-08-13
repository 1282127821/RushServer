package com.star.light.action.team;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;
import com.star.light.team.TeamMgr;

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