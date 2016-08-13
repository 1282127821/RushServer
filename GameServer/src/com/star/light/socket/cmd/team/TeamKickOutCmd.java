package com.star.light.socket.cmd.team;

import com.star.light.action.team.TeamKickOutAction;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;

import tbgame.pbmessage.GamePBMsg.TeamCommonMsg;

public class TeamKickOutCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		TeamCommonMsg netMsg = TeamCommonMsg.parseFrom(packet.getMsgBody());
		if (player.getTeam() != null) {	
			TeamMgr.getInstance().enDefaultQueue(new TeamKickOutAction(player, netMsg.getUserId()));
		}
	}
}