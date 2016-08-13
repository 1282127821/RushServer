package com.action.team;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.team.TeamMgr;

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
