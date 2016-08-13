package com.netmsg.guild;
//package com.star.light.socket.cmd.guild;
//
//import com.star.light.guild.GuildMgr;
//import com.star.light.player.GamePlayer;
//import com.star.light.proto.PBMessage;
//import com.star.light.protocol.Protocol;
//import com.star.light.socket.Cmd;
//import com.star.light.socket.cmd.AbstractCommand;
//
//@Cmd(code = Protocol.C_DISMISS_GUILD)
//public class DismissGuild extends AbstractCommand {
//
//	@Override
//	public void execute(GamePlayer player, PBMessage packet) throws Exception {
//		GuildMgr.getInstance().dismissGuild(player.getGuildId(), player);
//	}
//}