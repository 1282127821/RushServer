package com.star.light.action.team;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;
import com.star.light.team.TeamMgr;

public class TeamChangeLeaderAction extends Action {
	private GamePlayer player;
	private long userId;
	
	public TeamChangeLeaderAction(GamePlayer player, long userId) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
		this.userId = userId;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().changeLeaderTeam(player, userId);
	}
}
