package com.star.light.action.room;

import com.star.light.execaction.Action;
import com.star.light.protocol.Protocol;
import com.star.light.room.Room;
import com.star.light.socket.PBMessage;

public class EnterAnimationAction extends Action {
	private Room room;
	private PBMessage packet;

	public EnterAnimationAction(Room room, PBMessage packet) {
		super(room.getActionQueue());
		this.room = room;
		this.packet = packet;
	}

	@Override
	public void execute() 
	{
		if (room.isEnterAnimation == false) {
			room.isEnterAnimation = true;
			room.sendSyncMsg(0, Protocol.S_C_SYNC_PVP_STATE, packet);
		}

	}
}
