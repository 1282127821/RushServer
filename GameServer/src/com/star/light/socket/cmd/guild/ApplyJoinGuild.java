package com.star.light.socket.cmd.guild;

import com.star.light.action.guild.ApplyJoinGuildAction;
import com.star.light.guild.Guild;
import com.star.light.guild.GuildMgr;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.table.ConfigMgr;

import tbgame.pbmessage.GamePBMsg.GuildCommonMsg;

public class ApplyJoinGuild implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (player.getGuildId() != 0) {
			player.sendTips(1516);
			return;
		}

		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		Guild guild = GuildMgr.getInstance().getGuildById(netMsg.getGuildId());
		if (guild == null) {
			return;
		}

		if (GuildMgr.getInstance().getUserApplyCount(player.getUserId()) >= ConfigMgr.maxGuildApplyCount) {
			player.sendTips(1020);
			return;
		}

		guild.enqueue(new ApplyJoinGuildAction(guild, player));
	}
}