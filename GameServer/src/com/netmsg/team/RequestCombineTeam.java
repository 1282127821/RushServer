package com.netmsg.team;

import com.action.team.TeamCombineAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.TeamCommonMsg;
import com.player.GamePlayer;
import com.room.RoomMgr;

public class RequestCombineTeam implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		TeamCommonMsg netMsg = TeamCommonMsg.parseFrom(packet.getMsgBody());
		RoomMgr.getInstance().addAction(new TeamCombineAction(player, netMsg.getUserId()));
	}
}
