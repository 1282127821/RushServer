package com.netmsg.pvp;

import com.action.room.RoomCreateAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class CreateFuBen implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		RoomMgr.getInstance().addAction(new RoomCreateAction(player));
	}
}
