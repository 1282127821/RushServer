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
//@Cmd(code = Protocol.C_REQUEST_GUILD_EVENT_LIST)
//public class RequestGuildEventList extends AbstractCommand {
//
//	@Override
//	public void execute(GamePlayer player, PBMessage packet) throws Exception {
//		Guild guild = GuildMgr.getInstance().getGuildById(player.getGuildId());
//		if (guild != null) {
//			player.sendPacket(Protocol.S_C_ANSWER_GUILD_EVENT_LIST, guild.packGuildEveList());
//		}
//	}
//}