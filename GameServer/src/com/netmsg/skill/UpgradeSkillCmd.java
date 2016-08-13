package com.netmsg.skill;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.UpgradeSkillMsg;
import com.player.GamePlayer;

public class UpgradeSkillCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		UpgradeSkillMsg msg = UpgradeSkillMsg.parseFrom(packet.getMsgBody());
		int upgradeType = msg.getUpgradeType();
		if (upgradeType == 1 || upgradeType == 2) {
			player.getSkillInventory().skillLevelUp(msg.getSkillId(), upgradeType);
		}
	}
}
