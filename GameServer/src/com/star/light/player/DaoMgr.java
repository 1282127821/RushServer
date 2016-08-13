package com.star.light.player;

import com.star.light.friend.FriendDBInfoDao;
import com.star.light.guild.GuildApplyDao;
import com.star.light.guild.GuildDao;
import com.star.light.guild.GuildEventDao;
import com.star.light.guild.GuildMemberDao;
import com.star.light.guild.GuildTechDao;
import com.star.light.mail.MailDao;
import com.star.light.prop.PropInstanceDao;
import com.star.light.skill.FightSkillInfoDao;

public class DaoMgr {
	public static void init() {
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
