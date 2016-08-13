package com.star.light.action.room;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;

public class GetTotalRoomAction extends Action {
	private GamePlayer player;

	public GetTotalRoomAction(GamePlayer player) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
	}

	@Override
	public void execute() {
		RoomMgr.getInstance().packTotalRoomList(player);
	}
}

