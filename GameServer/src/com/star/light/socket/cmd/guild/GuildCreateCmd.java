package com.star.light.socket.cmd.guild;

import com.star.light.action.guild.GuildCreateAction;
import com.star.light.guild.Guild;
import com.star.light.guild.GuildMgr;
import com.star.light.player.GameConst;
import com.star.light.player.GamePlayer;
import com.star.light.player.ItemChangeType;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.table.ConfigMgr;
import com.star.light.table.DirtyData;

import tbgame.pbmessage.GamePBMsg.GuildCreateMsg;
import tbgame.pbmessage.GamePBMsg.TipInfoMsg;

public class GuildCreateCmd implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		if (!player.isOpenGuild()) {
			player.sendTips(1032);
			return;
		}

		GuildCreateMsg netMsg = GuildCreateMsg.parseFrom(packet.getMsgBody());
		long guildId = player.getGuildId();
		if (guildId != 0) {
			return;
		}

		String guildName = netMsg.getGuildName();
		if (!DirtyData.getInstance().isIllegal(guildName, GameConst.MAX_GUILDNAME_LEN)) {
			player.sendTips(1545);
			return;
		}
		
		String guildSlogan = netMsg.getGuildSlogan();
		if (guildSlogan.length() > 0 && !DirtyData.getInstance().isIllegal(guildSlogan, GameConst.MAX_GUILD_SLOGAN_LEN)) {
			player.sendTips(1545);
			return;
		}

		Guild guild = GuildMgr.getInstance().getGuildByName(guildName);
		if (guild != null) {
			TipInfoMsg.Builder tipMsg = TipInfoMsg.newBuilder();
			tipMsg.setTipId(1028);
			player.sendPacket(Protocol.S_C_FAILE_CREATE_GUILD, tipMsg);
			return;
		}

		if (!player.removeGold(ConfigMgr.createGuildCost, ItemChangeType.CREATE_GUILD)) {
			player.sendTips(1033);
			return;
		}
		
		GuildMgr.getInstance().enDefaultQueue(new GuildCreateAction(player, guildName, guildSlogan, netMsg.getGuildEmblem()));
	}
}
