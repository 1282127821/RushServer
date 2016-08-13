package com.netmsg.guild;
//package com.star.light.socket.cmd.guild;
//
//import com.star.light.entity.type.DiamondChangeType;
//import com.star.light.entity.type.ErrorCodeType;
//import com.star.light.entity.type.FightAttributeType;
//import com.star.light.entity.type.ItemChangeType;
//import com.star.light.guild.Guild;
//import com.star.light.guild.GuildMgr;
//import com.star.light.player.GamePlayer;
//import com.star.light.proto.PBMessage;
//import com.star.light.protocol.Protocol;
//import com.star.light.socket.Cmd;
//import com.star.light.socket.cmd.AbstractCommand;
//import com.star.light.table.ConfigMgr;
//
//import tbgame.pbmessage.GamePBMsg.GuildCommonMsg;
//
//@Cmd(code = Protocol.C_GUILD_DONATE)
//public class GuildDonate extends AbstractCommand {
//
//	@Override
//	public void execute(GamePlayer player, PBMessage packet) throws Exception {
//		Guild guild = GuildMgr.getInstance().getGuildById(player.getGuildId());
//		if (guild == null) {
//			player.sendErrorCode(Protocol.S_C_SYNC_GUILD_TECH, ErrorCodeType.FAIL);
//			return;
//		}
//
//		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getBytes());
//		int contribution = 0;
//		int techContribution = 0;
//		int goldDonateCount = player.playerDataInfo.getGoldDonateCount();
//		int diamondDonateCount = player.playerDataInfo.getDiamondDonateCount();
//		int donateType = netMsg.getDonateType();
//		if (donateType == 1 && goldDonateCount < ConfigMgr.guildGoldDonateCount && player.removeGold(ConfigMgr.guildGoldDonateCost, ItemChangeType.GUILD_GOLD_DONATE)) {
//			contribution = ConfigMgr.goldDonateContribution;
//			techContribution = ConfigMgr.goldDonateTechContribution;
//			player.setGoldDonateCount(goldDonateCount + 1);
//		} else if (donateType == 2 && diamondDonateCount < ConfigMgr.guildDiamondDonateCount && player.removeDiamond(ConfigMgr.guildDiamondDonateCost, DiamondChangeType.GUILD_DIAMOND_DONATE)) {
//			contribution = ConfigMgr.diamondDonateContribution;
//			techContribution = ConfigMgr.diamondDonateTechContribution;
//			player.setDiamondDonateCount(diamondDonateCount + 1);
//		}
//
//		int techId = netMsg.getTechId();
//		if (contribution > 0 && techContribution > 0 && techId > FightAttributeType.STRENGTH && techId <= FightAttributeType.COUNT) {
//			player.getGuildTechInventory().addTechContribution(guild, techId, contribution, techContribution, donateType);
//		}
//	}
//}