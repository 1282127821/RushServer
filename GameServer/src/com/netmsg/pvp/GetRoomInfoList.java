package com.netmsg.pvp;

import com.action.room.GetTotalRoomAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class GetRoomInfoList implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		RoomMgr.getInstance().addAction(new GetTotalRoomAction(player));
	}
}