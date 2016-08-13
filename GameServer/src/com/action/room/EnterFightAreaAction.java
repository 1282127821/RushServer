package com.action.room;

import java.util.List;

import com.execaction.Action;
import com.player.GamePlayer;
import com.room.Room;
import com.room.RoomPlayer;

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
