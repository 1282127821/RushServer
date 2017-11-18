package com.action.room;

import com.executor.AbstractAction;
import com.netmsg.PBMessage;
import com.protocol.Protocol;
import com.room.Room;

public class EnterAnimationAction extends AbstractAction
{
	private Room room;
	private PBMessage packet;

	public EnterAnimationAction(Room room, PBMessage packet)
	{
		this.room = room;
		this.packet = packet;
	}

	@Override
	public void execute()
	{
		if (room.isEnterAnimation == false)
		{
			room.isEnterAnimation = true;
			room.sendSyncMsg(0, Protocol.S_C_SYNC_PVP_STATE, packet);
		}
	}
}
