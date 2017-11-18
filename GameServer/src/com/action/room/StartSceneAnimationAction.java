package com.action.room;

import com.executor.AbstractAction;
import com.room.Room;
import com.room.RoomState;

public class StartSceneAnimationAction extends AbstractAction
{
	private Room room;

	public StartSceneAnimationAction(Room room)
	{
		this.room = room;
	}

	@Override
	public void execute()
	{
		if (room.isStartScene == false)
		{
			room.isStartScene = true;
			room.startPVPAI();
		}

		if (room.getRoomState() == RoomState.UNUSE)
		{
			room.updateRoomState(RoomState.USEING);
		}
	}
}
