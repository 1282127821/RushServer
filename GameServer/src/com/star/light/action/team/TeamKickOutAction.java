package com.star.light.action.team;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;
import com.star.light.team.TeamMgr;

public class TeamKickOutAction extends Action {
	private GamePlayer player;
	private long userId;
	
	public TeamKickOutAction(GamePlayer player, long userId) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
		this.userId = userId;
	}
	
	@Override
	public void execute() {
		TeamMgr.getInstance().kickOutTeam(player, userId);
	}
}
