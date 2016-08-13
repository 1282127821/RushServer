package com.action.room;

import com.execaction.Action;
import com.room.Room;
import com.room.RoomState;

public class StartSceneAnimationAction extends Action {
	private Room room;

	public StartSceneAnimationAction(Room room) {
		super(room.getActionQueue());
		this.room = room;
	}

	@Override
	public void execute() 
	{
		if (room.isStartScene == false) {
			room.isStartScene = true;
			room.startPVPAI();
		}

		if (room.getRoomState() == RoomState.UNUSE) {
			room.updateRoomState(RoomState.USEING);
		}
	}
}
