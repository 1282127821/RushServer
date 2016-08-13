package com.star.light.socket.cmd.guild;

import com.star.light.guild.GuildMgr;
import com.star.light.player.GameConst;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.table.DirtyData;

import tbgame.pbmessage.GamePBMsg.GuildCommonMsg;

public class ChangeGuildSlogan implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		String guildSlogan = netMsg.getGuildSlogan();
		if (guildSlogan.length() > 0 && !DirtyData.getInstance().isIllegal(guildSlogan, GameConst.MAX_GUILD_SLOGAN_LEN)) {
			player.sendTips(1545);
			return;
		}
			
		GuildMgr.getInstance().changeGuildSlogan(player, guildSlogan);
	}
}