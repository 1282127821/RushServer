package com.netmsg.team;

import com.action.team.TeamSynMessageAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.TeamSynMessageMsg_CSC;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.RoomMgr;

public class TeamSynMessageCMD implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		TeamSynMessageMsg_CSC netMsg = TeamSynMessageMsg_CSC.parseFrom(packet.getMsgBody());
		int curTaskId = netMsg.getTaskId();
		player.curTaskId = curTaskId;
		TeamSynMessageMsg_CSC.Builder taskMsg = TeamSynMessageMsg_CSC.newBuilder();
		taskMsg.setTaskId(curTaskId);
		player.sendPacket(Protocol.S_C_ACCEPT_TASK, taskMsg);
		if (player.getTeam() != null)
		{
			RoomMgr.getInstance().addAction(new TeamSynMessageAction(player, curTaskId));
		}
	}
}