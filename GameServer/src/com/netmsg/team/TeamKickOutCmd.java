package com.netmsg.team;

import com.action.team.TeamKickOutAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.TeamCommonMsg;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamKickOutCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		TeamCommonMsg netMsg = TeamCommonMsg.parseFrom(packet.getMsgBody());
		if (player.getTeam() != null) {	
			TeamMgr.getInstance().enDefaultQueue(new TeamKickOutAction(player, netMsg.getUserId()));
		}
	}
}