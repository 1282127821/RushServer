package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamSynMessageAction;
import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;

import tbgame.pbmessage.GamePBMsg.TeamSynMessageMsg_CSC;

public class TeamSynMessageCMD implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		TeamSynMessageMsg_CSC netMsg = TeamSynMessageMsg_CSC.parseFrom(packet.getMsgBody());
		int curTaskId = netMsg.getTaskId();
		player.curTaskId = curTaskId;
		TeamSynMessageMsg_CSC.Builder taskMsg = TeamSynMessageMsg_CSC.newBuilder();
		taskMsg.setTaskId(curTaskId);
		player.sendPacket(Protocol.S_C_ACCEPT_TASK, taskMsg);
		if (player.getTeam() != null) {
			TeamMgr.getInstance().enDefaultQueue(new TeamSynMessageAction(player, curTaskId));
		}
	}
}