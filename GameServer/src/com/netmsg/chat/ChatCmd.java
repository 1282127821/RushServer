package com.netmsg.chat;

import java.util.List;

import com.chat.ChatType;
import com.guild.Guild;
import com.guild.GuildMemberInfo;
import com.guild.GuildMgr;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.ChatMsg;
import com.player.GameConst;
import com.player.GamePlayer;
import com.player.WorldMgr;
import com.protocol.Protocol;
import com.team.Team;
import com.util.TimeUtil;

public class ChatCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		ChatMsg netMsg = ChatMsg.parseFrom(packet.getMsgBody());
		packet.setMsgId(Protocol.S_C_CHAT);
		String userName = player.getUserName();
		int chatType = netMsg.getChatType();
		if (chatType == ChatType.WORLD) {
			int curTime = TimeUtil.getSysCurSeconds();
			if (curTime < player.chatTime + GameConst.CHAT_TIME ) {
				player.sendTips(1025);
				return;
			}
			
			player.chatTime = curTime;
			broadcastWorldChat(userName, packet);
		} else if (chatType == ChatType.TEAM) {
			teamChat(player, userName, packet);
		} else if (chatType == ChatType.GUILD) {
			guildChat(player, userName, packet);
		} else if (chatType == ChatType.PRIVATE) {
			String toUserName = netMsg.getToUserName();
			if (toUserName.equals(userName)) {
				return;
			}
			
			GamePlayer gamePlayer = WorldMgr.getPlayerByName(toUserName);
			if (gamePlayer == null) {
				player.sendTips(1030);
				return;
			}
				
			if (gamePlayer.isBlackUser(userName)) {
				return;
			}
			
			player.sendPacket(packet);
			gamePlayer.sendPacket(packet);
		}
	}
	
	private void broadcastWorldChat(String userName, PBMessage packet) {
		List<GamePlayer> onlinePlayerList = WorldMgr.getOnlineList();
		for(GamePlayer gamePlayer : onlinePlayerList) {
			if (gamePlayer.isBlackUser(userName)) {
				continue;
			}
			
			gamePlayer.sendPacket(packet);
		}
	}
	
	private void teamChat(GamePlayer player, String userName, PBMessage packet) {
		Team team = player.getTeam();
		if (team == null) {
			player.sendTips(1026);
			return;
		}
		
		int curTime = TimeUtil.getSysCurSeconds();
		if (curTime < player.chatTime + GameConst.CHAT_TIME ) {
			player.sendTips(1025);
			return;
		}
		
		player.chatTime = curTime;
		
		List<GamePlayer> teamMemberList = team.getTeamMemberList();
		for (GamePlayer gamePlayer : teamMemberList) {
			if (gamePlayer.isBlackUser(userName)) {
				continue;
			}
			
			gamePlayer.sendPacket(packet);
		}
	}
	
	private void guildChat(GamePlayer player, String userName, PBMessage packet) {
		Guild guild = GuildMgr.getInstance().getGuildById(player.getGuildId());
		if (guild == null) {
			player.sendTips(1027);
			return;
		}
		
		int curTime = TimeUtil.getSysCurSeconds();
		if (curTime < player.chatTime + GameConst.CHAT_TIME ) {
			player.sendTips(1025);
			return;
		}
		
		player.chatTime = curTime;
		
		List<GuildMemberInfo> guildMemberList = guild.getGuildMemberList();
		for (GuildMemberInfo memberInfo : guildMemberList) {
			long memberUserId = memberInfo.getUserId();
			GamePlayer gamePlayer = WorldMgr.getOnlinePlayer(memberUserId);
			if (gamePlayer == null || gamePlayer.isBlackUser(userName)) {
				continue;
			}
			
			gamePlayer.sendPacket(packet);
		}
	}
}