package com.star.light.socket.cmd.guild;

import com.star.light.guild.GuildMgr;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.util.StringUtil;

import tbgame.pbmessage.GamePBMsg.GuildCommonMsg;

public class GuildSearch implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		String guildName = netMsg.getGuildName();
		if (StringUtil.isNotNullOrEmpty(guildName)) {
			GuildMgr.getInstance().searchGuildName(guildName, player);
		}
	}
}