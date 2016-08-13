package com.star.light.socket.cmd.guild;

import com.star.light.guild.GuildMgr;
import com.star.light.player.GamePlayer;
import com.star.light.player.WorldMgr;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.GuildCommonMsg;

public class AcceptApplyGuild implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		GamePlayer invitePlayer = WorldMgr.getPlayer(netMsg.getUserId());
		if (invitePlayer == null) {
			return;
		}

		GuildMgr.getInstance().acceptGuildApply(invitePlayer, player);
	}
}