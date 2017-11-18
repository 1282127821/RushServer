package com.action.room;

import com.executor.AbstractAction;
import com.player.GamePlayer;
import com.room.Room;

public class ExitRoomAction extends AbstractAction
{
	private Room room;
	private GamePlayer player;

	public ExitRoomAction(Room room, GamePlayer player)
	{
		this.room = room;
		this.player = player;
	}

	@Override
	public void execute()
	{
		room.exitRoom(player);
	}
}
