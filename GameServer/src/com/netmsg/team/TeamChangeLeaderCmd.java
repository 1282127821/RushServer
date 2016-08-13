package com.netmsg.team;

import com.action.team.TeamChangeLeaderAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.TeamCommonMsg;
import com.player.GamePlayer;
import com.team.TeamMgr;

public class TeamChangeLeaderCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (player.getTeam() != null) {
			TeamCommonMsg netMsg = TeamCommonMsg.parseFrom(packet.getMsgBody());
			TeamMgr.getInstance().enDefaultQueue(new TeamChangeLeaderAction(player, netMsg.getUserId()));
		}
	}
}
