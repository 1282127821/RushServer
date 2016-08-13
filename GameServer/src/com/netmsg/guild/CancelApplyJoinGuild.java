package com.netmsg.guild;

import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.GuildCommonMsg;
import com.player.GamePlayer;

public class CancelApplyJoinGuild implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		GuildMgr.getInstance().cancelApplyJoinGuild(netMsg.getGuildId(), player);
	}
}