package com.netmsg.guild;

import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;

public class AcceptAllGuildApply implements NetCmd
{

	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		GuildMgr.getInstance().acceptTotalGuildApply(player);
	}
}