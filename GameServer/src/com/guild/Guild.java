package com.guild;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.db.DBOption;
import com.pbmessage.GamePBMsg.GuildApplyListMsg;
import com.pbmessage.GamePBMsg.GuildApplyMsg;
import com.pbmessage.GamePBMsg.GuildEventListMsg;
import com.pbmessage.GamePBMsg.GuildEventMsg;
import com.pbmessage.GamePBMsg.GuildInfoMsg;
import com.pbmessage.GamePBMsg.GuildMemberListMsg;
import com.pbmessage.GamePBMsg.GuildMemberMsg;
import com.player.DaoMgr;
import com.player.GameConst;
import com.player.GamePlayer;
import com.player.PlayerSynchType;
import com.protocol.Protocol;
import com.table.GuildLevelInfo;
import com.table.GuildLevelInfoMgr;
import com.util.Log;
import com.util.TimeUtil;

public class Guild
{
	/**
	 * 公会的公有信息
	 */
	private GuildInfo guildInfo;

	/**
	 * 公会成员列表
	 */
	private List<GuildMemberInfo> guildMemberList;

	/**
	 * 公会申请列表
	 */
	private List<GuildApplyInfo> guildApplyList;

	/**
	 * 公会事件列表
	 */
	private List<GuildEventInfo> guildEventList;

	/**
	 * 会长信息
	 */
	private GuildMemberInfo leaderInfo;

	public Guild()
	{
		guildMemberList = new ArrayList<GuildMemberInfo>();
		guildApplyList = new ArrayList<GuildApplyInfo>();
		guildEventList = new ArrayList<GuildEventInfo>();
		guildInfo = new GuildInfo();
	}

	public void init(GuildInfo guildInfo, List<GuildMemberInfo> memberList, List<GuildApplyInfo> applyList, List<GuildEventInfo> eventList)
	{
		this.guildInfo = guildInfo;
		if (memberList != null)
		{
			guildMemberList = memberList;
		}

		if (applyList != null)
		{
			guildApplyList = applyList;
		}

		if (eventList != null)
		{
			guildEventList = eventList;
		}

		for (GuildMemberInfo info : guildMemberList)
		{
			if (info.getPower() == 1)
			{
				leaderInfo = info;
				break;
			}
		}
	}

	public void createGuild(GuildInfo guildInfo, GamePlayer player)
	{
		this.guildInfo = guildInfo;
		addGuildMember(player, GuildPower.GUILD_LEADER);
		addGuildEvent(1501, player.getUserName());
	}

	/**
	 * 获取公会Id
	 */
	public long getGuildId()
	{
		return guildInfo.getGuildId();
	}

	public List<GuildMemberInfo> getGuildMemberList()
	{
		return guildMemberList;
	}

	public List<GuildApplyInfo> getGuildApplyList()
	{
		return guildApplyList;
	}

	/**
	 * 获取公会名字
	 */
	public String getGuildName()
	{
		return guildInfo.getGuildName();
	}

	public GuildInfo getGuildInfo()
	{
		return guildInfo;
	}

	public void setGuildLeaderInfo(GuildMemberInfo leaderInfo)
	{
		this.leaderInfo = leaderInfo;
	}

	public int getMaxGuildMemberCount()
	{
		return GameConst.GUILD_BASE_MEMBER_COUNT + guildInfo.getGuildLv();
	}

	public boolean isInApplyList(long userId)
	{
		for (GuildApplyInfo info : guildApplyList)
		{
			if (info.getUserId() == userId)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 打包公会信息
	 */
	public GuildInfoMsg.Builder packGuildInfo(long userId)
	{
		GuildInfoMsg.Builder infoMsg = GuildInfoMsg.newBuilder();
		infoMsg.setGuildId(guildInfo.getGuildId());
		infoMsg.setGuildName(guildInfo.getGuildName());
		infoMsg.setGuildEmblem(guildInfo.getGuildEmblem());
		infoMsg.setMemberCount(getMemberCount());
		infoMsg.setTotalExp(guildInfo.getTotalExp());
		infoMsg.setGuildSlogan(guildInfo.getGuildSlogan());
		infoMsg.setGuildLv(guildInfo.getGuildLv());
		infoMsg.setGuildLeaderName(leaderInfo.getUserName());
		infoMsg.setGuildLeaderLv(leaderInfo.getPlayerLv());
		infoMsg.setCreateTime(guildInfo.getCreateTime());
		int applyState = 0;
		if (userId != 0 && isInApplyList(userId))
		{
			applyState = 1;
		}
		infoMsg.setApplyState(applyState);
		return infoMsg;
	}

	/**
	 * 获取公会某个成员
	 */
	public GuildMemberInfo getGuildMember(long userId)
	{
		for (GuildMemberInfo info : guildMemberList)
		{
			if (info.getUserId() == userId)
			{
				return info;
			}
		}
		return null;
	}

	/**
	 * 获取帮会的人数
	 */
	public int getMemberCount()
	{
		return guildMemberList.size();
	}

	public void addGuildMember(GamePlayer player, int power)
	{
		GuildMemberInfo guildMember = new GuildMemberInfo();
		guildMember.setUserId(player.getUserId());
		guildMember.setUserName(player.getUserName());
		guildMember.setVipLv(player.getVipLv());
		guildMember.setContribution(0);
		guildMember.setFightStrength(player.getFightStrength());
		guildMember.setPlayerLv(player.getPlayerLv());
		guildMember.setJobId(player.getJobId());
		int logoutTime = 0;
		player.setGuildId(guildInfo.getGuildId());
		if (!player.isOnline())
		{
			logoutTime = player.getLogoutTime();
		}
		guildMember.setLogoutTime(logoutTime);
		if (power == 1)
		{
			leaderInfo = guildMember;
		}
		else
		{
			addGuildExp(3);
		}
		guildMember.setPower(power);
		guildMember.setOp(DBOption.INSERT);
		guildMemberList.add(guildMember);
	}

	public GuildMemberListMsg.Builder packGuildMember()
	{
		GuildMemberListMsg.Builder netMsg = GuildMemberListMsg.newBuilder();
		for (GuildMemberInfo memberInfo : guildMemberList)
		{
			netMsg.addGuildMemList(packOneGuildMember(memberInfo));
		}

		return netMsg;
	}

	public GuildMemberMsg.Builder packOneGuildMember(GuildMemberInfo memberInfo)
	{
		GuildMemberMsg.Builder infoMsg = GuildMemberMsg.newBuilder();
		infoMsg.setUserId(memberInfo.getUserId());
		infoMsg.setUserName(memberInfo.getUserName());
		infoMsg.setVipLv(memberInfo.getVipLv());
		infoMsg.setPower(memberInfo.getPower());
		infoMsg.setPlayerLv(memberInfo.getPlayerLv());
		infoMsg.setJobId(memberInfo.getJobId());
		infoMsg.setFightStrength(memberInfo.getFightStrength());
		infoMsg.setContribution(memberInfo.getContribution());
		infoMsg.setLogoutTime(memberInfo.getLogoutTime());
		return infoMsg;
	}

	public GuildApplyListMsg.Builder packGuildApply()
	{
		GuildApplyListMsg.Builder netMsg = GuildApplyListMsg.newBuilder();
		for (GuildApplyInfo applyInfo : guildApplyList)
		{
			GuildApplyMsg.Builder infoMsg = GuildApplyMsg.newBuilder();
			infoMsg.setUserId(applyInfo.getUserId());
			infoMsg.setUserName(applyInfo.getUserName());
			infoMsg.setVipLv(applyInfo.getVipLv());
			infoMsg.setJobId(applyInfo.getJobId());
			infoMsg.setPlayerLv(applyInfo.getPlayerLv());
			infoMsg.setFightStrength(applyInfo.getFightStrength());
			netMsg.addGuildApplyList(infoMsg);
		}
		return netMsg;
	}

	public GuildEventListMsg.Builder packGuildEveList()
	{
		GuildEventListMsg.Builder netMsg = GuildEventListMsg.newBuilder();
		for (GuildEventInfo eventInfo : guildEventList)
		{
			GuildEventMsg.Builder infoMsg = GuildEventMsg.newBuilder();
			infoMsg.setEventDesc(eventInfo.getEventDesc());
			infoMsg.setEventTime(eventInfo.getEventTime());
			netMsg.addGuildEventList(infoMsg);
		}
		return netMsg;
	}

	/**
	 * 公会某成员增加贡献度
	 */
	public void addContribution(GamePlayer player, int contribution, int donateType)
	{
		int infoId = 1551;
		if (donateType == 2)
		{
			infoId = 1552;
		}
		addGuildEvent(infoId, player.getUserName(), contribution);
		long userId = player.getUserId();
		GuildMemberInfo memberInfo = getGuildMember(userId);
		int curContribution = memberInfo.getContribution() + contribution;
		memberInfo.setContribution(curContribution);
		guildInfo.setTotalExp(guildInfo.getTotalExp() + contribution);
		GuildInfoMsg.Builder infoMsg = GuildInfoMsg.newBuilder();
		player.sendPacket(Protocol.S_C_RETURN_GUILD_INFO, infoMsg);
	}

	public void addContribution(int value)
	{
		guildInfo.setTotalExp(guildInfo.getTotalExp() + value);
	}

	public void addGuildExp(int addExp)
	{
		short curGuildLv = guildInfo.getGuildLv();
		if (curGuildLv >= GameConst.MAX_GUILD_LEVEL)
		{
			return;
		}

		int totalGuildExp = guildInfo.getTotalExp() + addExp;
		GuildLevelInfo guildLevelInfo = GuildLevelInfoMgr.getInstance().getGuildLevelInfo(curGuildLv);
		if (guildLevelInfo == null)
		{
			return;
		}

		int remainExp = totalGuildExp - guildLevelInfo.needExp;
		if (remainExp > 0)
		{
			curGuildLv += 1;
			if (curGuildLv == GameConst.MAX_GUILD_LEVEL)
			{
				remainExp = 0;
			}
			guildInfo.setGuildLv(curGuildLv);
			guildInfo.setTotalExp(remainExp);
		}
		else
		{
			guildInfo.setTotalExp(totalGuildExp);
		}
	}

	/**
	 * 获取申请信息的条数
	 */
	public int getApplyCount()
	{
		return guildApplyList.size();
	}

	/**
	 * 增加一个公会申请信息
	 */
	public void applyJoinGuild(GamePlayer player)
	{
		if (getMemberCount() >= getMaxGuildMemberCount())
		{
			player.sendTips(1023);
			return;
		}

		long userId = player.getUserId();
		if (isInApplyList(userId))
		{
			player.sendTips(1543);
			return;
		}

		// if (guildInfo.getIsAudit() == 2) {
		GuildApplyInfo applyInfo = new GuildApplyInfo();
		applyInfo.setUserId(userId);
		applyInfo.setUserName(player.getUserName());
		applyInfo.setPlayerLv(player.getPlayerLv());
		applyInfo.setJobId(player.getJobId());
		applyInfo.setFightStrength(player.getFightStrength());
		applyInfo.setVipLv(player.getVipLv());
		applyInfo.setApplyTime(TimeUtil.getSysCurSecond());
		applyInfo.setOp(DBOption.INSERT);
		guildApplyList.add(applyInfo);
		GuildInfoMsg.Builder netMsg = GuildInfoMsg.newBuilder();
		netMsg.setGuildId(getGuildId());
		netMsg.setApplyState(1);
		player.sendPacket(Protocol.S_C_APPLY_JOIN_GUILD, netMsg);
		// } else {
		// addGuildMember(player, GuildPower.GUILD_MEMBER);
		// addGuildEvent(1503, player.getUserName());
		// GuildMgr.getInstance().deleteUserApply(userId);
		// GuildMgr.getInstance().syncGuildInfo(guildInfo.getGuildId(), player);
		// GuildCommonMsg.Builder netMsg = GuildCommonMsg.newBuilder();
		// netMsg.setGuildId(guildInfo.getGuildId());
		// netMsg.setGuildName(guildInfo.getGuildName());
		// player.sendPacket(Protocol.S_C_SUCCESS_JOIN_GUILD, netMsg);
		// }
	}

	/**
	 * 删除一个公会申请
	 */
	public void deleteGuildApply(long guildId, long userId)
	{
		Iterator<GuildApplyInfo> iter = guildApplyList.iterator();
		while (iter.hasNext())
		{
			GuildApplyInfo info = iter.next();
			if (info.getUserId() == userId)
			{
				iter.remove();
				break;
			}
		}

		DaoMgr.guildApplyDao.deleteGuildApplyInfo(guildId, userId);
	}

	/**
	 * 删除掉公会的所有申请
	 */
	public void deleteAllGuildApply(long guildId)
	{
		guildApplyList.clear();
		DaoMgr.guildApplyDao.deleteAllGuildApplyInfo(guildId);
	}

	/**
	 * 删除一个公会成员
	 */
	public void deleteGuildMember(GamePlayer player)
	{
		long userId = player.getUserId();
		Iterator<GuildMemberInfo> iter = guildMemberList.iterator();
		while (iter.hasNext())
		{
			GuildMemberInfo info = iter.next();
			if (info.getUserId() == userId)
			{
				iter.remove();
				break;
			}
		}
		player.setGuildId(0);
		DaoMgr.guildMemerDao.deleteGuildMember(userId);
	}

	/**
	 * 获得公会副会长的个数
	 */
	public int getDeputyChairmanCount()
	{
		int totalCount = 0;
		for (GuildMemberInfo info : guildMemberList)
		{
			if (info.getPower() == 2)
			{
				totalCount++;
			}
		}
		return totalCount;
	}

	/**
	 * 改变公会的公告信息
	 */
	public void setGuildSlogan(String guildSlogan)
	{
		guildInfo.setGuildSlogan(guildSlogan);
	}

	/**
	 * 是否拥有这个权力
	 */
	public boolean isHavePower(long userId)
	{
		GuildMemberInfo memberInfo = getGuildMember(userId);
		return memberInfo != null && memberInfo.getPower() <= GuildPower.GUILD_LEADER;
	}

	/**
	 * 是否为会长
	 */
	public boolean isLeader(long userId)
	{
		return leaderInfo.getUserId() == userId;
	}

	/**
	 * 删除掉公会内的所有数据
	 */
	public void deleteAllInfo(long guildId, GamePlayer player)
	{
		deleteGuildMember(player);
		deleteAllGuildApply(guildId);
		guildEventList.clear();
		DaoMgr.guildEventDao.deleteAllGuildEvent(guildId);
	}

	public void addGuildEvent(int eventId, Object... args)
	{
		GuildEventInfo eventInfo = new GuildEventInfo();
		// eventInfo.setEventDesc(SystemMsgMgr.getInstance().getMsgInfoByMsgId(eventId,
		// args));
		eventInfo.setEventTime(TimeUtil.getSysCurSecond());
		eventInfo.setOp(DBOption.INSERT);
		if (guildEventList.size() >= 50)
		{
			GuildEventInfo deleteEvent = guildEventList.remove(0);
			DaoMgr.guildEventDao.deleteGuildEventInfo(guildInfo.getGuildId(), deleteEvent.getEventTime());
		}

		guildEventList.add(eventInfo);
	}

	public void syncUpdateMemInfo(long userId, short propertyType, int propertyValue)
	{
		GuildMemberInfo memberInfo = getGuildMember(userId);
		if (memberInfo != null)
		{
			if (propertyType == PlayerSynchType.LOGOUT_TIME)
			{
				memberInfo.setLogoutTime(propertyValue);
			}
			else if (propertyType == PlayerSynchType.LEVEL)
			{
				memberInfo.setPlayerLv(propertyValue);
			}
			else if (propertyType == PlayerSynchType.FIGHT_STRENGTH)
			{
				memberInfo.setFightStrength(propertyValue);
			}
			else if (propertyType == PlayerSynchType.VIPLV)
			{
				memberInfo.setVipLv(propertyValue);
			}
		}
	}

	public GuildMemberInfo getLeaderInfo()
	{
		return leaderInfo;
	}

	public int getSelfContribution(long userId)
	{
		GuildMemberInfo info = getGuildMember(userId);
		return info != null ? info.getContribution() : 0;
	}

	/**
	 * 保存公会的信息
	 */
	public void saveGuildInfo()
	{
		long guildId = getGuildId();
		try
		{
			if (guildInfo.getOp() == DBOption.INSERT)
			{
				DaoMgr.guildDao.addGuildInfo(guildId, guildInfo);
			}

			if (guildInfo.getOp() == DBOption.UPDATE)
			{
				DaoMgr.guildDao.updateGuildInfo(guildId, guildInfo);
			}
		}
		catch (Exception e)
		{
			Log.error("保存公会数据,guildId: " + guildId, e);
		}

		try
		{
			List<GuildMemberInfo> saveList = new ArrayList<GuildMemberInfo>(guildMemberList);
			for (GuildMemberInfo memberInfo : saveList)
			{
				if (memberInfo.getOp() == DBOption.INSERT)
				{
					DaoMgr.guildMemerDao.addGuildMemberInfo(guildId, memberInfo);
				}

				if (memberInfo.getOp() == DBOption.UPDATE)
				{
					DaoMgr.guildMemerDao.updateMemberInfo(guildId, memberInfo);
				}
			}
		}
		catch (Exception e)
		{
			Log.error("保存公会成员数据,guildId: " + guildId, e);
		}

		try
		{
			List<GuildApplyInfo> saveList = new ArrayList<GuildApplyInfo>(guildApplyList);
			for (GuildApplyInfo applyInfo : saveList)
			{
				if (applyInfo.getOp() == DBOption.INSERT)
				{
					DaoMgr.guildApplyDao.addGuildApplyInfo(guildId, applyInfo);
				}
			}
		}
		catch (Exception e)
		{
			Log.error("保存公会申请数据,guildId: " + guildId, e);
		}

		try
		{
			List<GuildEventInfo> saveList = new ArrayList<GuildEventInfo>(guildEventList);
			for (GuildEventInfo eventInfo : saveList)
			{
				if (eventInfo.getOp() == DBOption.INSERT)
				{
					DaoMgr.guildEventDao.addGuildEventInfo(guildId, eventInfo);
				}
			}
		}
		catch (Exception e)
		{
			Log.error("保存公会事件数据,guildId: " + guildId, e);
		}
	}
}
