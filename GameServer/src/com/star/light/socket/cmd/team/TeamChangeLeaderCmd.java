package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamChangeLeaderAction;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;

import tbgame.pbmessage.GamePBMsg.TeamCommonMsg;

public class TeamChangeLeaderCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (player.getTeam() != null) {
			TeamCommonMsg netMsg = TeamCommonMsg.parseFrom(packet.getMsgBody());
			TeamMgr.getInstance().enDefaultQueue(new TeamChangeLeaderAction(player, netMsg.getUserId()));
		}
	}
}
