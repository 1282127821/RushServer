package com.netmsg.guild;

import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.GuildCommonMsg;
import com.player.GameConst;
import com.player.GamePlayer;
import com.table.DirtyData;

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