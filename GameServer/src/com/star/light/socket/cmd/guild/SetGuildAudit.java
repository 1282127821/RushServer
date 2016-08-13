//package com.star.light.socket.cmd.guild;
//
//import com.star.light.entity.type.ErrorCodeType;
//import com.star.light.guild.Guild;
//import com.star.light.guild.GuildMgr;
//import com.star.light.player.GamePlayer;
//import com.star.light.proto.PBMessage;
//import com.star.light.protocol.Protocol;
//import com.star.light.socket.Cmd;
//import com.star.light.socket.cmd.AbstractCommand;
//
//import tbgame.pbmessage.GamePBMsg.GuildInfoMsg;
//
//@Cmd(code = Protocol.C_SET_GUILD_AUDIT)
//public class SetGuildAudit extends AbstractCommand {
//
//	@Override
//	public void execute(GamePlayer player, PBMessage packet) throws Exception {
//		Guild guild = GuildMgr.getInstance().getGuildById(player.getGuildId());
//		if (guild == null) {
//			player.sendErrorCode(Protocol.S_C_SUCCESS_SET_GUILD_AUDIT, ErrorCodeType.FAIL);
//			return;
//		}
//
//		long userId = player.getUserId();
//		if (!guild.isLeader(userId)) {
//			player.sendErrorCode(Protocol.S_C_SUCCESS_SET_GUILD_AUDIT, ErrorCodeType.FAIL);
//			return;
//		}
//
//		GuildInfoMsg netMsg = GuildInfoMsg.parseFrom(packet.getBytes());
//		int isAudit = netMsg.getIsAudit();
//		guild.getGuildInfo().setIsAudit(isAudit);
//		GuildInfoMsg.Builder clientMsg = GuildInfoMsg.newBuilder();
//		clientMsg.setIsAudit(isAudit);
//		player.sendPacket(Protocol.S_C_SUCCESS_SET_GUILD_AUDIT, clientMsg);
//	}
//}