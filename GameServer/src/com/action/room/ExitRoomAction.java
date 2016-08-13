package com.action.room;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.Room;

public class ExitRoomAction extends Action {
	private Room room;
	private GamePlayer player;

	public ExitRoomAction(Room room, GamePlayer player) {
		super(room.getActionQueue());
		this.room = room;
		this.player = player;
	}

	@Override
	public void execute() 
	{
		room.exitRoom(player);
	}
}
