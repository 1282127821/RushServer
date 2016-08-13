package com.star.light.action.room;

import com.star.light.execaction.Action;
import com.star.light.room.Room;
import com.star.light.room.RoomState;

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
