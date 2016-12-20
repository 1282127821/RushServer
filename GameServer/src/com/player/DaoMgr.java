package com.player;

import com.friend.FriendDBInfoDao;
import com.guild.GuildApplyDao;
import com.guild.GuildDao;
import com.guild.GuildEventDao;
import com.guild.GuildMemberDao;
import com.guild.GuildTechDao;
import com.mail.MailDao;
import com.prop.PropInstanceDao;
import com.skill.FightSkillInfoDao;

public class DaoMgr
{
	public static void init()
	{
		DaoMgr.playerInfoDao.resetState();
	}

	public static final PlayerInfoDao playerInfoDao = new PlayerInfoDao();
	public static final PropInstanceDao itemInfoDao = new PropInstanceDao();
	public static final FightSkillInfoDao fightSkillInfoDao = new FightSkillInfoDao();
	public static final FriendDBInfoDao friendDao = new FriendDBInfoDao();
	public static final MailDao mailDao = new MailDao();
	public static final GuildDao guildDao = new GuildDao();
	public static final GuildApplyDao guildApplyDao = new GuildApplyDao();
	public static final GuildMemberDao guildMemerDao = new GuildMemberDao();
	public static final GuildEventDao guildEventDao = new GuildEventDao();
	public static final GuildTechDao guildTeachDao = new GuildTechDao();
}