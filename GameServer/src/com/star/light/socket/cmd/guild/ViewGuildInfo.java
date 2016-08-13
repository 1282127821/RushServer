package com.star.light.socket.cmd.guild;

import com.star.light.guild.GuildMgr;
import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class ViewGuildInfo implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GuildMgr.getInstance().viewGuildInfo(player);
	}
}