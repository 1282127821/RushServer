package com.netmsg.skill;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SkillChainMsg;
import com.player.GamePlayer;

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
