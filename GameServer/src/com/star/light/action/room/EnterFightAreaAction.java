package com.star.light.action.room;

import java.util.List;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.room.Room;
import com.star.light.room.RoomPlayer;

public class EnterFightAreaAction extends Action {
	private Room room;
	private GamePlayer player;

	public EnterFightAreaAction(Room room, GamePlayer player) {
		super(room.getActionQueue());
		this.room = room;
		this.player = player;
	}

	@Override
	public void execute() 
	{
		List<RoomPlayer> roomPlayerList = room.getTotalRoomPlayer();
		for (RoomPlayer roomPlayer : roomPlayerList) {
			if (roomPlayer.player.getUserId() == player.getUserId()) {
				roomPlayer.isCanAttack = true;
				break;
			}
		}
		
		room.startPVPAI();
	}
}
