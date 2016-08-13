package com.netmsg;

import java.util.concurrent.ConcurrentHashMap;

import com.netmsg.chat.AddBlackUser;
import com.netmsg.chat.ChatCmd;
import com.netmsg.chat.DeleteBlackUser;
import com.netmsg.chat.GMOrderCmd;
import com.netmsg.friend.AddFriend;
import com.netmsg.friend.DeleteFriend;
import com.netmsg.friend.SearchFriend;
import com.netmsg.friend.SearchNearUser;
import com.netmsg.friend.ViewFriendInfo;
import com.netmsg.guild.AcceptAllGuildApply;
import com.netmsg.guild.AcceptApplyGuild;
import com.netmsg.guild.ApplyJoinGuild;
import com.netmsg.guild.AppointGuildPower;
import com.netmsg.guild.CancelApplyJoinGuild;
import com.netmsg.guild.ChangeGuildSlogan;
import com.netmsg.guild.GuildCreateCmd;
import com.netmsg.guild.GuildSearch;
import com.netmsg.guild.KickoutGuildMember;
import com.netmsg.guild.LeaveGuild;
import com.netmsg.guild.RejectAllGuildApply;
import com.netmsg.guild.RejectApplyGuild;
import com.netmsg.guild.RequestTotalGuildList;
import com.netmsg.guild.ViewGuildInfo;
import com.netmsg.guild.ViewGuildTotalApply;
import com.netmsg.mail.GetAllMailAttach;
import com.netmsg.mail.GetOneMailAttach;
import com.netmsg.mail.RequestTotalMailInfo;
import com.netmsg.player.PlayerLoginReceiveCmd;
import com.netmsg.player.PlayerLogoutCmd;
import com.netmsg.player.UserValidateCmd;
import com.netmsg.prop.EquipAdvanceCmd;
import com.netmsg.prop.EquipBlessCmd;
import com.netmsg.prop.EquipInlayCmd;
import com.netmsg.prop.PropOperatorCmd;
import com.netmsg.pvp.CreateFuBen;
import com.netmsg.pvp.EnterFightArea;
import com.netmsg.pvp.GetRoomInfoList;
import com.netmsg.pvp.LeaveFuBen;
import com.netmsg.pvp.PVPPlayerDie;
import com.netmsg.pvp.PVPReady;
import com.netmsg.pvp.RobberyFuBen;
import com.netmsg.pvp.StartSceneAnimation;
import com.netmsg.pvp.SyncAction;
import com.netmsg.pvp.SyncCreateProjectile;
import com.netmsg.pvp.SyncFlyItem;
import com.netmsg.pvp.SyncFlyPosition;
import com.netmsg.pvp.SyncHurt;
import com.netmsg.pvp.SyncPVPState;
import com.netmsg.pvp.SyncPosition;
import com.netmsg.pvp.SyncShieldHP;
import com.netmsg.pvp.SyncUseSkillLink;
import com.netmsg.pvp.UseRecoverProp;
import com.netmsg.scene.EnterScene;
import com.netmsg.scene.LeaveScene;
import com.netmsg.scene.SceneMove;
import com.netmsg.skill.SetSkillChainInfoCmd;
import com.netmsg.skill.UpgradeSkillCmd;
import com.netmsg.team.RequestCombineTeam;
import com.netmsg.team.RequestTeamInfo;
import com.netmsg.team.SyncTeamListCmd;
import com.netmsg.team.TeamChangeLeaderCmd;
import com.netmsg.team.TeamCreateCmd;
import com.netmsg.team.TeamDisbanCmd;
import com.netmsg.team.TeamJoinCmd;
import com.netmsg.team.TeamKickOutCmd;
import com.netmsg.team.TeamLeaveCmd;
import com.netmsg.team.TeamSynMessageCMD;
import com.protocol.Protocol;
import com.util.GameLog;

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
