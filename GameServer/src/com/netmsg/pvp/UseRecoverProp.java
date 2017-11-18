package com.netmsg.pvp;

import com.action.room.AddPlayerHPAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.Room;
import com.room.RoomMgr;

public class UseRecoverProp implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		Room room = player.getRoom();
		if (room != null)
		{
			RoomMgr.getInstance().addAction(new AddPlayerHPAction(room, player));
			room.sendSyncMsg(0, Protocol.S_C_USE_RECOVERY_DRUG, packet);
		}
	}
}
