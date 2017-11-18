package com.netmsg.pvp;

import com.action.room.RobberyFuBenAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.RoomInfoMsg;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class RobberyFuBen implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		RoomMgr.getInstance().addAction(new RobberyFuBenAction(player, RoomInfoMsg.parseFrom(packet.getMsgBody())));
	}
}
