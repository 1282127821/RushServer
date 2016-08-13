package com.star.light.socket.cmd.skill;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.UpgradeSkillMsg;

public class UpgradeSkillCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		UpgradeSkillMsg msg = UpgradeSkillMsg.parseFrom(packet.getMsgBody());
		int upgradeType = msg.getUpgradeType();
		if (upgradeType == 1 || upgradeType == 2) {
			player.getSkillInventory().skillLevelUp(msg.getSkillId(), upgradeType);
		}
	}
}
