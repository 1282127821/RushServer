package com.action.room;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.RoomMgr;

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

