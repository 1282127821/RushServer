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
//@Cmd(code = Protocol.C_ANSWER_GUILD_INVITE)
//public class AnswerInviteJoinGuild extends AbstractCommand {
//
//	@Override
//	public void execute(GamePlayer player, PBMessage packet) throws Exception {
//		GuildCommonMsg netMsg = GuildCommonMsg.parseFrom(packet.getBytes());
//		GamePlayer invitePlayer = WorldMgr.getOnlinePlayer(netMsg.getUserId());
//		if (invitePlayer != null) {
//			long guildId = netMsg.getGuildId();
//			boolean isSuccess = netMsg.getIsSuccess();
//			if (isSuccess) {
//				GuildMgr.getInstance().joinGuild(guildId, player, invitePlayer);
//			} else {
//				invitePlayer.sendTips(1519);
//			}
//		} else {
//			player.sendErrorCode(Protocol.S_C_SUCCESS_JOIN_GUILD, ErrorCodeType.FAIL);
//		}
//	}
//}