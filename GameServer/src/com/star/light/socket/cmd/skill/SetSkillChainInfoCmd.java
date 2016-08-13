package com.star.light.socket.cmd.skill;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.SkillChainMsg;

public class SetSkillChainInfoCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {	
		SkillChainMsg netMsg = SkillChainMsg.parseFrom(packet.getMsgBody());
		int operType = netMsg.getOperType();
		if (operType == 1) {
			player.getSkillInventory().setFightSkillChain(netMsg);
		} else if (operType == 2) {
			player.getSkillInventory().exchangeSkillChain(netMsg.getSkillSrcPos(), netMsg.getSkillDestPos());
		}
	}
}
