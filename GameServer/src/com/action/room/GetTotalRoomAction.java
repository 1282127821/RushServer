package com.action.room;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class GetTotalRoomAction extends AbstractAction
{
	private GamePlayer player;

	public GetTotalRoomAction(GamePlayer player)
	{
		this.player = player;
	}

	@Override
	public void execute()
	{
		RoomMgr.getInstance().packTotalRoomList(player);
	}
}
