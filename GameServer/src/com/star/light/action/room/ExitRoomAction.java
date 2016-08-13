package com.star.light.action.room;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;

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
