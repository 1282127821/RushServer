package com.netmsg.guild;

import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.GuildCommonMsg;
import com.player.GamePlayer;
import com.player.WorldMgr;

public class AppointGuildPower implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		GamePlayer appointPlayer = WorldMgr.getPlayer(netMsg.getUserId());
		if (appointPlayer != null) {
			GuildMgr.getInstance().appointGuildPower(appointPlayer, player);
		}
	}
}