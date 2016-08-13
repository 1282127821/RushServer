package com.action.room;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.Room;
import com.room.RoomPlayer;

public class AddPlayerHPAction extends Action {
	private Room room;
	private GamePlayer player;

	public AddPlayerHPAction(Room room, GamePlayer player) {
		super(room.getActionQueue());
		this.room = room;
		this.player = player;
	}

	@Override
	public void execute() 
	{
		RoomPlayer roomPlayer = room.getRoomPlayer(player.getUserId());
		if (roomPlayer != null) {
			roomPlayer.playerHP += 300;
		}
	}
}