package com.netmsg.guild;

import com.action.guild.ApplyJoinGuildAction;
import com.guild.Guild;
import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.GuildCommonMsg;
import com.player.GamePlayer;
import com.room.RoomMgr;
import com.table.ConfigMgr;

public class ApplyJoinGuild implements NetCmd
{

	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		if (player.getGuildId() != 0)
		{
			player.sendTips(1516);
			return;
		}

		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getMsgBody());
		Guild guild = GuildMgr.getInstance().getGuildById(netMsg.getGuildId());
		if (guild == null)
		{
			return;
		}

		if (GuildMgr.getInstance().getUserApplyCount(player.getUserId()) >= ConfigMgr.maxGuildApplyCount)
		{
			player.sendTips(1020);
			return;
		}

		RoomMgr.getInstance().addAction(new ApplyJoinGuildAction(guild, player));
	}
}