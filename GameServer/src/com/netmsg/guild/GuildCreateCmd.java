package com.netmsg.guild;

import com.action.guild.GuildCreateAction;
import com.guild.Guild;
import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.GuildCreateMsg;
import com.pbmessage.GamePBMsg.TipInfoMsg;
import com.player.GameConst;
import com.player.GamePlayer;
import com.player.ItemChangeType;
import com.protocol.Protocol;
import com.table.ConfigMgr;
import com.table.DirtyData;

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
