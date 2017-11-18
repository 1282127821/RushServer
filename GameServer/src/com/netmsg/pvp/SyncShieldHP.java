package com.netmsg.pvp;

import com.action.room.AddShieldHPAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SyncPVPCommonMsg;
import com.player.GamePlayer;
import com.room.Room;
import com.room.RoomMgr;

public class SyncShieldHP implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		Room room = player.getRoom();
		if (room != null)
		{
			SyncPVPCommonMsg netMsg = SyncPVPCommonMsg.parseFrom(packet.getMsgBody());
			RoomMgr.getInstance().addAction(new AddShieldHPAction(room, player, netMsg.getOperType()));
		}
	}
}