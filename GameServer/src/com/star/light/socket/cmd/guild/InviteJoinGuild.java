//package com.star.light.socket.cmd.guild;
//
//import com.star.light.entity.type.ErrorCodeType;
//import com.star.light.guild.GuildMgr;
//import com.star.light.player.GamePlayer;
//import com.star.light.player.WorldMgr;
//import com.star.light.proto.PBMessage;
//import com.star.light.protocol.Protocol;
//import com.star.light.socket.Cmd;
//import com.star.light.socket.cmd.AbstractCommand;
//
//import tbgame.pbmessage.GamePBMsg.GuildCommonMsg;
//
//@Cmd(code = Protocol.C_INVITE_JOIN_GUILD)
//public class InviteJoinGuild extends AbstractCommand {
//
//	@Override
//	public void execute(GamePlayer player, PBMessage packet) throws Exception {
//		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getBytes());
//		String charName = netMsg.getCharName();
//		GamePlayer invitedPlayer = WorldMgr.getPlayerByName(charName);
//		if (invitedPlayer == null) {
//			player.sendTips(1518);
//			player.sendErrorCode(Protocol.S_C_INVITE_JOIN_GUILD, ErrorCodeType.FAIL);
//			return;
//		}
//
//		long otherGuildId = invitedPlayer.getGuildId();
//		if (otherGuildId != 0) {
//			int infoId = 1516;
//			if (otherGuildId == player.getGuildId()) {
//				infoId = 1542;
//			}
//			player.sendTips(infoId);
//			player.sendErrorCode(Protocol.S_C_INVITE_JOIN_GUILD, ErrorCodeType.FAIL);
//			return;
//		}
//
//		if (invitedPlayer.isOpenGuild()) {
//			player.sendTips(1517);
//			player.sendErrorCode(Protocol.S_C_INVITE_JOIN_GUILD, ErrorCodeType.FAIL);
//			return;
//		}
//
//		GuildMgr.getInstance().inviteJoinGuild(invitedPlayer, player);
//	}
//}