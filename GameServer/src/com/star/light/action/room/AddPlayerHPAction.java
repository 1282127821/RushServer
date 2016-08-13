package com.star.light.action.room;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;
import com.star.light.room.RoomPlayer;

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