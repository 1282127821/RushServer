package com.star.light.socket.cmd;

import java.util.concurrent.ConcurrentHashMap;

import com.star.light.protocol.Protocol;
import com.star.light.socket.cmd.chat.AddBlackUser;
import com.star.light.socket.cmd.chat.ChatCmd;
import com.star.light.socket.cmd.chat.DeleteBlackUser;
import com.star.light.socket.cmd.chat.GMOrderCmd;
import com.star.light.socket.cmd.friend.AddFriend;
import com.star.light.socket.cmd.friend.DeleteFriend;
import com.star.light.socket.cmd.friend.SearchFriend;
import com.star.light.socket.cmd.friend.SearchNearUser;
import com.star.light.socket.cmd.friend.ViewFriendInfo;
import com.star.light.socket.cmd.guild.AcceptAllGuildApply;
import com.star.light.socket.cmd.guild.AcceptApplyGuild;
import com.star.light.socket.cmd.guild.ApplyJoinGuild;
import com.star.light.socket.cmd.guild.AppointGuildPower;
import com.star.light.socket.cmd.guild.CancelApplyJoinGuild;
import com.star.light.socket.cmd.guild.ChangeGuildSlogan;
import com.star.light.socket.cmd.guild.GuildCreateCmd;
import com.star.light.socket.cmd.guild.GuildSearch;
import com.star.light.socket.cmd.guild.KickoutGuildMember;
import com.star.light.socket.cmd.guild.LeaveGuild;
import com.star.light.socket.cmd.guild.RejectAllGuildApply;
import com.star.light.socket.cmd.guild.RejectApplyGuild;
import com.star.light.socket.cmd.guild.RequestTotalGuildList;
import com.star.light.socket.cmd.guild.ViewGuildInfo;
import com.star.light.socket.cmd.guild.ViewGuildTotalApply;
import com.star.light.socket.cmd.mail.GetAllMailAttach;
import com.star.light.socket.cmd.mail.GetOneMailAttach;
import com.star.light.socket.cmd.mail.RequestTotalMailInfo;
import com.star.light.socket.cmd.player.PlayerLoginReceiveCmd;
import com.star.light.socket.cmd.player.PlayerLogoutCmd;
import com.star.light.socket.cmd.player.UserValidateCmd;
import com.star.light.socket.cmd.prop.EquipAdvanceCmd;
import com.star.light.socket.cmd.prop.EquipBlessCmd;
import com.star.light.socket.cmd.prop.EquipInlayCmd;
import com.star.light.socket.cmd.prop.PropOperatorCmd;
import com.star.light.socket.cmd.pvp.CreateFuBen;
import com.star.light.socket.cmd.pvp.EnterFightArea;
import com.star.light.socket.cmd.pvp.GetRoomInfoList;
import com.star.light.socket.cmd.pvp.LeaveFuBen;
import com.star.light.socket.cmd.pvp.PVPPlayerDie;
import com.star.light.socket.cmd.pvp.PVPReady;
import com.star.light.socket.cmd.pvp.RobberyFuBen;
import com.star.light.socket.cmd.pvp.StartSceneAnimation;
import com.star.light.socket.cmd.pvp.SyncAction;
import com.star.light.socket.cmd.pvp.SyncCreateProjectile;
import com.star.light.socket.cmd.pvp.SyncFlyItem;
import com.star.light.socket.cmd.pvp.SyncFlyPosition;
import com.star.light.socket.cmd.pvp.SyncHurt;
import com.star.light.socket.cmd.pvp.SyncPVPState;
import com.star.light.socket.cmd.pvp.SyncPosition;
import com.star.light.socket.cmd.pvp.SyncShieldHP;
import com.star.light.socket.cmd.pvp.SyncUseSkillLink;
import com.star.light.socket.cmd.pvp.UseRecoverProp;
import com.star.light.socket.cmd.scene.EnterScene;
import com.star.light.socket.cmd.scene.LeaveScene;
import com.star.light.socket.cmd.scene.SceneMove;
import com.star.light.socket.cmd.skill.SetSkillChainInfoCmd;
import com.star.light.socket.cmd.skill.UpgradeSkillCmd;
import com.star.light.socket.cmd.team.RequestCombineTeam;
import com.star.light.socket.cmd.team.RequestTeamInfo;
import com.star.light.socket.cmd.team.SyncTeamListCmd;
import com.star.light.socket.cmd.team.TeamChangeLeaderCmd;
import com.star.light.socket.cmd.team.TeamCreateCmd;
import com.star.light.socket.cmd.team.TeamDisbanCmd;
import com.star.light.socket.cmd.team.TeamJoinCmd;
import com.star.light.socket.cmd.team.TeamKickOutCmd;
import com.star.light.socket.cmd.team.TeamLeaveCmd;
import com.star.light.socket.cmd.team.TeamSynMessageCMD;
import com.star.light.util.GameLog;

/**
 * 网络消息管理器
 */
public final class NetMsgMgr {
	private ConcurrentHashMap<Short, NetCmd> netMsgMap = new ConcurrentHashMap<Short, NetCmd>();
	private static NetMsgMgr instance = new NetMsgMgr();

	public static NetMsgMgr getInstance() {
		return instance;
	}

	/**
	 * 加载网络消息集合
	 */
	public boolean load() {
		registerNetMsg(Protocol.C_REGISTER, new GameRegister());
		registerNetMsg(Protocol.C_S_PLAYER_LOGIN, new PlayerLoginReceiveCmd());
		registerNetMsg(Protocol.C_S_PLAYER_LOGOUT, new PlayerLogoutCmd());
		registerNetMsg(Protocol.C_S_PLAYER_KEY, new UserValidateCmd());
		registerNetMsg(Protocol.C_S_ADD_FRIEND, new AddFriend());
		registerNetMsg(Protocol.C_S_DEL_FRIEND, new DeleteFriend());
		registerNetMsg(Protocol.C_S_SEARCH_FRIEND, new SearchFriend());
		registerNetMsg(Protocol.C_S_VIEW_NEAR_USER, new SearchNearUser());
		registerNetMsg(Protocol.C_S_VIEW_FRIEND_INFO, new ViewFriendInfo());
		registerNetMsg(Protocol.C_S_GET_ALL_MAIL_ATTACH, new GetAllMailAttach());
		registerNetMsg(Protocol.C_S_GET_ONE_MAIL_ATTACH, new GetOneMailAttach());
		registerNetMsg(Protocol.C_S_REQUEST_TOTAL_MAIL_INFO, new RequestTotalMailInfo());
		registerNetMsg(Protocol.C_S_ITEM_OPERATOR, new PropOperatorCmd());
		registerNetMsg(Protocol.C_S_EQUIP_ADVANCE, new EquipAdvanceCmd());
		registerNetMsg(Protocol.C_S_EQUIP_BLESS, new EquipBlessCmd());
		registerNetMsg(Protocol.C_S_EQUIP_INLAY, new EquipInlayCmd());
		registerNetMsg(Protocol.C_S_SET_FIGHT_SKILL_CHAIN, new SetSkillChainInfoCmd());
		registerNetMsg(Protocol.C_S_UPGRADE_SKILL, new UpgradeSkillCmd());
		registerNetMsg(Protocol.C_S_REQUEST_TEAM_INFO, new RequestTeamInfo());
		registerNetMsg(Protocol.C_S_REQUEST_COMBINE_TEAM, new RequestCombineTeam());
		registerNetMsg(Protocol.C_SYNC_TEAMLIST, new SyncTeamListCmd());
		registerNetMsg(Protocol.C_CHANGELEADER_TEAM, new TeamChangeLeaderCmd());
		registerNetMsg(Protocol.C_CREATE_TEAM, new TeamCreateCmd());
		registerNetMsg(Protocol.C_DISBAN_TEAM, new TeamDisbanCmd());
		registerNetMsg(Protocol.C_JOIN_TEAM, new TeamJoinCmd());
		registerNetMsg(Protocol.C_KICKOUT_TEAM, new TeamKickOutCmd());
		registerNetMsg(Protocol.C_LEAVE_TEAM, new TeamLeaveCmd());
		registerNetMsg(Protocol.C_S_SYNMESSAGE_TEAM, new TeamSynMessageCMD());
		registerNetMsg(Protocol.C_CREATE_INSTANCE, new CreateFuBen());
		registerNetMsg(Protocol.C_S_ROOM_INFO_LIST, new GetRoomInfoList());
		registerNetMsg(Protocol.C_LEAVE_INSTANCE, new LeaveFuBen());
		registerNetMsg(Protocol.C_S_PVP_READY, new PVPReady());
		registerNetMsg(Protocol.C_S_ROBBERY_INSTANCE, new RobberyFuBen());
		registerNetMsg(Protocol.C_S_SYNC_ACTION, new SyncAction());
		registerNetMsg(Protocol.C_S_SYNC_CREATEPROJECTILE, new SyncCreateProjectile());
		registerNetMsg(Protocol.C_S_FLY_ITEM, new SyncFlyItem());
		registerNetMsg(Protocol.C_S_SYNC_FLY_POSITION, new SyncFlyPosition());
		registerNetMsg(Protocol.C_S_PVP_PLAYER_DIE, new PVPPlayerDie());
		registerNetMsg(Protocol.C_S_SYNC_HURT, new SyncHurt());
		registerNetMsg(Protocol.C_S_SYNC_POSITION, new SyncPosition());
		registerNetMsg(Protocol.C_S_SYNC_PVP_STATE, new SyncPVPState());
		registerNetMsg(Protocol.C_S_SYNC_PVP_SHIELDHP, new SyncShieldHP());
		registerNetMsg(Protocol.C_S_SYNC_USE_SKILL_LINK, new SyncUseSkillLink());
		registerNetMsg(Protocol.C_S_USE_RECOVERY_DRUG, new UseRecoverProp());
		registerNetMsg(Protocol.C_S_PVP_START_SCENE_ANIMATION, new StartSceneAnimation());
		registerNetMsg(Protocol.C_S_ENTER_FIGHT_AREA, new EnterFightArea());
		registerNetMsg(Protocol.C_S_ACCEPT_GUILD_APPLY, new AcceptApplyGuild());
		registerNetMsg(Protocol.C_S_ACCEPT_TOTAL_GUILD_APPLY, new AcceptAllGuildApply());
		registerNetMsg(Protocol.C_S_APPLY_JOIN_GUILD, new ApplyJoinGuild());
		registerNetMsg(Protocol.C_S_APPOINT_GUILD_POWER, new AppointGuildPower());
		registerNetMsg(Protocol.C_S_CANCLE_GUILD_APPLY, new CancelApplyJoinGuild());
		registerNetMsg(Protocol.C_S_CHANGE_GUILD_SLOGAN, new ChangeGuildSlogan());
		registerNetMsg(Protocol.C_S_CREATE_GUILD, new GuildCreateCmd());
		registerNetMsg(Protocol.C_S_SEARCH_GUILD, new GuildSearch());
		registerNetMsg(Protocol.C_S_KICK_OUT_GUILD_MEMBER, new KickoutGuildMember());
		registerNetMsg(Protocol.C_S_LEAVE_GUILD, new LeaveGuild());
		registerNetMsg(Protocol.C_S_REJECT_TOTAL_GUILD_APPLY, new RejectAllGuildApply());
		registerNetMsg(Protocol.C_S_REJECT_GUILD_APPLY, new RejectApplyGuild());
		registerNetMsg(Protocol.C_S_REQUEST_TOTAL_GUILD_INFO, new RequestTotalGuildList());
		registerNetMsg(Protocol.C_S_VIEW_GUILD_INFO, new ViewGuildInfo());
		registerNetMsg(Protocol.C_S_VIEW_TOTAL_GUILD_APPLY, new ViewGuildTotalApply());
		registerNetMsg(Protocol.C_S_ENTER_SCENE, new EnterScene());
		registerNetMsg(Protocol.C_S_SCENE_MOVE, new SceneMove());
		registerNetMsg(Protocol.C_S_LEAVE_SCNE, new LeaveScene());
		registerNetMsg(Protocol.C_S_CHAT, new ChatCmd());
		registerNetMsg(Protocol.C_S_ADD_USER_BLACK, new AddBlackUser());
		registerNetMsg(Protocol.C_S_DELETE_BLACK_USER, new DeleteBlackUser());
		registerNetMsg(Protocol.C_S_GM_ORDER, new GMOrderCmd());
		return true;
	}

	private void registerNetMsg(short codeId, NetCmd netCmd) {
		if (netMsgMap.containsKey(codeId)) {
			GameLog.error("网络协议号重复:  0x: " + Integer.toHexString(codeId));
			System.exit(0);
		}
		
		netMsgMap.put(codeId, netCmd);
	}

	/**
	 * 缓存中获取命令
	 */
	public NetCmd getNetCmd(short codeId) {
		return netMsgMap.get(codeId);
	}
}
