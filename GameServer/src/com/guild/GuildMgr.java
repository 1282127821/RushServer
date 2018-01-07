package com.guild;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.BaseServer;
import com.action.guild.GuildDeleteAction;
import com.action.guild.SyncGuildMemberInfo;
import com.db.DBOption;
import com.pbmessage.GamePBMsg.GuildAcceptApplyMsg;
import com.pbmessage.GamePBMsg.GuildCommonMsg;
import com.pbmessage.GamePBMsg.GuildInfoMsg;
import com.pbmessage.GamePBMsg.GuildListMsg;
import com.pbmessage.GamePBMsg.GuildMsg;
import com.player.DaoMgr;
import com.player.GamePlayer;
import com.player.PlayerSynchType;
import com.player.WorldMgr;
import com.protocol.Protocol;
import com.room.RoomMgr;
import com.util.TimeUtil;

public final class GuildMgr
{
	private List<Guild> guildList = new ArrayList<Guild>();
	private static GuildMgr instance = new GuildMgr();

	public static GuildMgr getInstance()
	{
		return instance;
	}

	/**
	 * 服务器启动的时候把所有帮会基本信息和成员列表查询出来
	 */
	public void loadAllGuild()
	{
		List<GuildInfo> guildInfos = DaoMgr.guildDao.getAllGuildInfo();
		Map<Long, List<GuildMemberInfo>> guildMemMap = DaoMgr.guildMemerDao.getAllGuildMember();
		Map<Long, List<GuildApplyInfo>> guildApplyMap = DaoMgr.guildApplyDao.getAllGuildApply();
		Map<Long, List<GuildEventInfo>> guildEventMap = DaoMgr.guildEventDao.getAllGuildEvent();

		for (GuildInfo info : guildInfos)
		{
			Guild guild = new Guild();
			long guildId = info.getGuildId();
			guild.init(info, guildMemMap.get(guildId), guildApplyMap.get(guildId), guildEventMap.get(guildId));
			guildList.add(guild);
		}
	}

	/**
	 * 创建一个公会
	 */
	public void createGuild(GamePlayer player, String guildName, String guildSlogan, short guildEmblem)
	{
		GuildInfo guildInfo = new GuildInfo();
		guildInfo.setGuildId(BaseServer.IDWORK.nextId());
		guildInfo.setGuildName(guildName);
		guildInfo.setGuildSlogan(guildSlogan);
		guildInfo.setGuildEmblem(guildEmblem);
		guildInfo.setIsAudit(true);
		guildInfo.setGuildLv((short) 1);
		guildInfo.setTotalExp(0);
		guildInfo.setCreateTime(TimeUtil.getSysCurSecond());
		guildInfo.setExist(true);
		guildInfo.setOp(DBOption.INSERT);

		Guild guild = new Guild();
		guild.createGuild(guildInfo, player);
		guildList.add(guild);
		player.sendPacket(Protocol.S_C_CREATE_GUILD, guild.packGuildInfo(0));
	}

	/**
	 * 客户端发送过来请求查看公会列表
	 */
	public void packAllGuildList(GamePlayer player)
	{
		GuildListMsg.Builder guildListMsg = GuildListMsg.newBuilder();
		int guildListCount = guildList.size();
		long userId = player.getUserId();
		for (Guild guild : guildList)
		{
			guildListMsg.addGuildList(guild.packGuildInfo(userId));
		}
		guildListMsg.setGuildCount(guildListCount);
		guildListMsg.setApplyCount(getUserApplyCount(userId));
		player.sendPacket(Protocol.S_C_REQUEST_TOTAL_GUILD_INFO, guildListMsg);
	}

	/**
	 * 打包自己公会的信息
	 */
	public void packSelfGuildInfo(GamePlayer player)
	{
		Guild guild = getGuildById(player.getGuildId());
		if (guild != null)
		{
			player.sendPacket(Protocol.S_C_SELF_GUILD_INFO, guild.packGuildInfo(0));
		}
	}

	/**
	 * 根据公会Id获得对应的公会
	 */
	public Guild getGuildById(long guildId)
	{
		for (Guild guild : guildList)
		{
			if (guild.getGuildId() == guildId)
			{
				return guild;
			}
		}

		return null;
	}

	/**
	 * 根据公会名字获得对应的公会
	 */
	public Guild getGuildByName(String guildName)
	{
		for (Guild guild : guildList)
		{
			if (guild.getGuildName().equals(guildName))
			{
				return guild;
			}
		}

		return null;
	}

	/**
	 * 根据公会名字获得对应的公会，不区分大小写
	 */
	public Guild getGuildByNameIgnoreCase(String guildName)
	{
		for (Guild guild : guildList)
		{
			if (guild.getGuildName().equalsIgnoreCase(guildName))
			{
				return guild;
			}
		}

		return null;
	}

	/**
	 * 根据公会Id获得对应公会的名字
	 */
	public String getGuildNameById(long guildId)
	{
		Guild guild = getGuildById(guildId);
		return guild != null ? guild.getGuildName() : "";
	}

	/**
	 * 查看某个公会的详细信息
	 */
	public void viewGuildInfo(GamePlayer player)
	{
		Guild guild = getGuildById(player.getGuildId());
		if (guild == null)
		{
			return;
		}

		GuildMsg.Builder netMsg = GuildMsg.newBuilder();
		netMsg.setGuildInfo(guild.packGuildInfo(0));
		netMsg.setGuildMemberInfo(guild.packGuildMember());
		player.sendPacket(Protocol.S_C_VIEW_GUILD_INFO, netMsg);
	}

	/**
	 * 搜索某个指定名称的公会
	 */
	public void searchGuildName(String guildName, GamePlayer player)
	{
		Guild guild = getGuildByNameIgnoreCase(guildName);
		if (guild == null)
		{
			player.sendTips(1019);
			return;
		}

		player.sendPacket(Protocol.S_C_SEARCH_GUILD, guild.packGuildInfo(player.getUserId()));
	}

	/**
	 * 获得玩家在所有公会中的申请加入公会数量
	 */
	public int getUserApplyCount(long userId)
	{
		int applyCount = 0;
		for (Guild guild : guildList)
		{
			if (guild.isInApplyList(userId))
			{
				applyCount++;
			}
		}

		return applyCount;
	}

	/**
	 * 删除玩家在所有公会的所有申请
	 */
	public void deleteUserApply(long userId)
	{
		for (Guild guild : guildList)
		{
			if (guild.isInApplyList(userId))
			{
				guild.deleteGuildApply(guild.getGuildId(), userId);
			}
		}
	}

	/**
	 * 取消申请加入公会
	 */
	public void cancelApplyJoinGuild(long guildId, GamePlayer player)
	{
		Guild guild = getGuildById(guildId);
		if (guild == null)
		{
			return;
		}

		guild.deleteGuildApply(guildId, player.getUserId());
		GuildInfoMsg.Builder netMsg = GuildInfoMsg.newBuilder();
		netMsg.setGuildId(guildId);
		netMsg.setApplyState(0);
		player.sendPacket(Protocol.S_C_CANCLE_GUILD_APPLY, netMsg);
	}

	/**
	 * 邀请玩家加入公会
	 */
	public void inviteJoinGuild(GamePlayer invitePlayer, GamePlayer player)
	{
		long guildId = player.getGuildId();
		Guild guild = getGuildById(guildId);
		if (guild == null)
		{
			return;
		}

		if (guild.getMemberCount() >= guild.getMaxGuildMemberCount())
		{
			player.sendTips(1023);
			return;
		}

		long userId = player.getUserId();
		if (!guild.isHavePower(userId))
		{
			return;
		}

		player.sendTips(1530);
		GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		netMsg.setGuildId(guildId);
		netMsg.setGuildName(getGuildNameById(guildId));
		netMsg.setUserId(userId);
		// invitePlayer.sendPacket(Protocol.S_C_INVITE_JOIN_GUILD, netMsg);
	}

	public void acceptTotalGuildApply(GamePlayer player)
	{
		long guildId = player.getGuildId();
		Guild guild = getGuildById(guildId);
		if (guild == null)
		{
			return;
		}

		if (!guild.isLeader(player.getUserId()))
		{
			return;
		}

		GuildAcceptApplyMsg.Builder netMsg = GuildAcceptApplyMsg.newBuilder();
		List<GuildApplyInfo> guildApplyList = guild.getGuildApplyList();
		for (GuildApplyInfo applyInfo : guildApplyList)
		{
			if (guild.getMemberCount() >= guild.getMaxGuildMemberCount())
			{
				player.sendTips(1023);
				break;
			}

			long otherUserId = applyInfo.getUserId();
			GamePlayer invitePlayer = WorldMgr.getPlayer(otherUserId);
			long otherGuild = invitePlayer.getGuildId();
			if (otherGuild == 0)
			{
				guild.addGuildMember(invitePlayer, GuildPower.GUILD_MEMBER);
				guild.addGuildEvent(1503, invitePlayer.getUserName());
				if (invitePlayer.isOnline())
				{
					GuildCommonMsg.Builder commonMsg = GuildCommonMsg.newBuilder();
					commonMsg.setGuildId(guildId);
					commonMsg.setGuildName(getGuildNameById(guildId));
					invitePlayer.sendPacket(Protocol.S_C_SUCCESS_GUILD_APPLY, commonMsg);
				}
			}

			netMsg.addUserIdList(otherUserId);
		}

		for (long otherUserId : netMsg.getUserIdListList())
		{
			deleteUserApply(otherUserId);
		}
		player.sendPacket(Protocol.S_C_ACCEPT_TOTAL_GUILD_APPLY, netMsg);
	}

	/**
	 * 接受玩家的加入公会申请
	 */
	public void acceptGuildApply(GamePlayer invitePlayer, GamePlayer player)
	{
		long otherGuild = invitePlayer.getGuildId();
		if (otherGuild > 0)
		{
			player.sendTips(1021);
			return;
		}

		long guildId = player.getGuildId();
		if (otherGuild == guildId)
		{
			player.sendTips(1022);
			return;
		}

		Guild guild = getGuildById(guildId);
		if (guild == null)
		{
			return;
		}

		long otherUserId = invitePlayer.getUserId();
		if (!guild.isLeader(player.getUserId()))
		{
			return;
		}

		if (guild.getMemberCount() >= guild.getMaxGuildMemberCount())
		{
			player.sendTips(1023);
			return;
		}

		if (!guild.isInApplyList(otherUserId))
		{
			player.sendTips(1544);
			return;
		}

		GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		netMsg.setUserId(otherUserId);
		player.sendPacket(Protocol.S_C_ACCEPT_GUILD_APPLY, netMsg);
		guild.addGuildMember(invitePlayer, GuildPower.GUILD_MEMBER);
		guild.addGuildEvent(1503, invitePlayer.getUserName());
		deleteUserApply(otherUserId);
		if (invitePlayer.isOnline())
		{
			GuildCommonMsg.Builder commonMsg = GuildCommonMsg.newBuilder();
			commonMsg.setGuildId(guildId);
			commonMsg.setGuildName(getGuildNameById(guildId));
			invitePlayer.sendPacket(Protocol.S_C_SUCCESS_GUILD_APPLY, commonMsg);
		}
	}

	/**
	 * 拒绝加入公会申请
	 */
	public void rejectGuildApply(GamePlayer player, long userId)
	{
		long guildId = player.getGuildId();
		Guild guild = getGuildById(guildId);
		if (guild != null && guild.isLeader(player.getUserId()))
		{
			guild.deleteGuildApply(guildId, userId);
			GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
			netMsg.setUserId(userId);
			player.sendPacket(Protocol.S_C_REJECT_GUILD_APPLY, netMsg);
		}
	}

	/**
	 * 拒绝所有的公会申请
	 */
	public void rejectAllGuildApply(GamePlayer player)
	{
		long guildId = player.getGuildId();
		Guild guild = getGuildById(guildId);
		if (guild != null && guild.isLeader(player.getUserId()))
		{
			guild.deleteAllGuildApply(guildId);
			player.sendPacket(Protocol.S_C_REJECT_TOTAL_GUILD_APPLY, null);
		}
	}

	/**
	 * 加入某个公会
	 */
	public void joinGuild(long guildId, GamePlayer player, GamePlayer invitePlayer)
	{
		Guild guild = getGuildById(guildId);
		if (guild == null || player.getGuildId() > 0)
		{
			return;
		}

		long userId = player.getUserId();
		guild.addGuildMember(player, GuildPower.GUILD_MEMBER);
		guild.addGuildEvent(1503, player.getUserName());
		deleteUserApply(userId);
		GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		netMsg.setGuildId(guildId);
		netMsg.setGuildName(getGuildNameById(guildId));
		player.sendPacket(Protocol.S_C_SUCCESS_JOIN_GUILD, netMsg);
		GuildMemberInfo guildMemberInfo = guild.getGuildMember(userId);
		invitePlayer.sendPacket(Protocol.S_C_SUCCESS_INVITE_JOIN_GUILD, guild.packOneGuildMember(guildMemberInfo));
	}

	/**
	 * 发送所有公会申请列表
	 */
	public void sendAllApplyList(GamePlayer player)
	{
		Guild guild = getGuildById(player.getGuildId());
		if (guild != null)
		{
			player.sendPacket(Protocol.S_C_VIEW_TOTAL_GUILD_APPLY, guild.packGuildApply());
		}
	}

	/**
	 * 任命公会某个成员的职位
	 */
	public void appointGuildPower(GamePlayer appointPlayer, GamePlayer player)
	{
		Guild guild = getGuildById(player.getGuildId());
		if (guild == null)
		{
			return;
		}

		long userId = player.getUserId();
		if (!guild.isLeader(userId))
		{
			return;
		}

		long otherUserId = appointPlayer.getUserId();
		GuildMemberInfo memberInfo = guild.getGuildMember(otherUserId);
		GuildMemberInfo selfMemberInfo = guild.getGuildMember(userId);
		if (memberInfo == null || selfMemberInfo == null)
		{
			return;
		}

		if (appointPlayer.isOnline())
		{
			appointPlayer.sendPacket(Protocol.S_C_BE_GUILD_LEADER, null);
		}
		memberInfo.setPower(GuildPower.GUILD_LEADER);
		selfMemberInfo.setPower(GuildPower.GUILD_MEMBER);
		guild.setGuildLeaderInfo(memberInfo);
		player.sendPacket(Protocol.S_C_APPOINT_GUILD_POWER, null);
	}

	/**
	 * 离开某个公会
	 */
	public void leaveGuild(GamePlayer player)
	{
		Guild guild = getGuildById(player.getGuildId());
		if (guild == null)
		{
			return;
		}

		long userId = player.getUserId();
		if (guild.isLeader(userId))
		{
			return;
		}

		guild.deleteGuildMember(player);
		guild.addGuildEvent(1504, player.getUserName());
		GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		netMsg.setGuildId(-1);
		player.sendPacket(Protocol.S_C_LEAVE_GUILD, netMsg);
	}

	/**
	 * 删掉某个公会
	 */
	public void deleteGuild(long guildId)
	{
		Iterator<Guild> iter = guildList.iterator();
		while (iter.hasNext())
		{
			Guild info = iter.next();
			if (info.getGuildId() == guildId)
			{
				iter.remove();
				break;
			}
		}

		DaoMgr.guildDao.deleteGuild(guildId);
	}

	/**
	 * 解散某个公会
	 */
	public void dismissGuild(long guildId, GamePlayer player)
	{
		Guild guild = getGuildById(guildId);
		long userId = player.getUserId();
		if (guild == null || !guild.isLeader(userId))
		{
			return;
		}

		if (guild.getMemberCount() > 1)
		{
			player.sendTips(1521);
			return;
		}

		guild.deleteAllInfo(guildId, player);
		RoomMgr.getInstance().addAction(new GuildDeleteAction(guildId));
		GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		netMsg.setGuildId(-1);
		player.sendPacket(Protocol.S_C_ANSWER_DISMISS_GUILD, netMsg);
	}

	/**
	 * 踢出公会的某个成员
	 */
	public void kickoutGuildMem(GamePlayer kickPlayer, GamePlayer player)
	{
		long guildId = player.getGuildId();
		Guild guild = getGuildById(guildId);
		if (guild == null)
		{
			return;
		}

		if (guild.isLeader(player.getUserId()))
		{
			long userId = kickPlayer.getUserId();
			guild.deleteGuildMember(kickPlayer);
			guild.addGuildEvent(1504, kickPlayer.getUserName());
			GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
			netMsg.setUserId(userId);
			player.sendPacket(Protocol.S_C_ANSWER_KICK_OUT_MEMBER, netMsg);
			if (kickPlayer.isOnline())
			{
				kickPlayer.sendPacket(Protocol.S_C_BE_KICK_OUT, null);
			}
		}
	}

	/**
	 * 改变公会的公告信息
	 */
	public void changeGuildSlogan(GamePlayer player, String guildSlogan)
	{
		Guild guild = getGuildById(player.getGuildId());
		if (guild == null || !guild.isLeader(player.getUserId()))
		{
			return;
		}

		guild.setGuildSlogan(guildSlogan);
		GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		netMsg.setGuildSlogan(guildSlogan);
		player.sendPacket(Protocol.S_C_CHANGE_GUILD_SLOGAN, netMsg);
	}

	public void syncGuildMemInfo(long guildId, long userId, short propertyType, int propertyValue)
	{
		Guild guild = getGuildById(guildId);
		if (guild != null)
		{
			RoomMgr.getInstance().addAction(new SyncGuildMemberInfo(guild, userId, propertyType, propertyValue));
		}
	}

	public void syncGuildInfo(long guildId, GamePlayer player)
	{
		Guild guild = getGuildById(guildId);
		if (guild == null)
		{
			return;
		}

		syncGuildMemInfo(guildId, player.getUserId(), PlayerSynchType.LOGOUT_TIME, 0);
		GuildInfoMsg.Builder infoMsg = GuildInfoMsg.newBuilder();
		GuildInfo guildInfo = guild.getGuildInfo();
		infoMsg.setGuildId(guildInfo.getGuildId());
		infoMsg.setGuildName(guildInfo.getGuildName());
		infoMsg.setGuildEmblem(guildInfo.getGuildEmblem());
		infoMsg.setMemberCount(guild.getMemberCount());
		infoMsg.setTotalExp(guildInfo.getTotalExp());
		infoMsg.setGuildSlogan(guildInfo.getGuildSlogan());
		infoMsg.setGuildLeaderName(guild.getLeaderInfo().getUserName());
		player.sendPacket(Protocol.S_C_RETURN_GUILD_INFO, infoMsg);
	}

	/**
	 * 保存帮会的信息
	 */
	public void saveAllGuild()
	{
		for (Guild guild : guildList)
		{
			guild.saveGuildInfo();
		}
	}
}
