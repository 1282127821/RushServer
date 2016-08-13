package com.player;

/**
 * 游戏中使用的一些公共的变量
 */
public interface GameConst {
	/**
	 * 单个职业所拥有的职业技能数量
	 */
	short PER_JOB_SKILL_COUNT = 9;

	/**
	 * 每个技能链拥有的技能个数
	 */
	short PERSKILLCHAINCOUNT = 6;

	/**
	 * 万分几率
	 */
	double TEN_THOUSAND_RATE = 0.0001;

	/**
	 * 用于万分率计算使用
	 */
	short TEN_THOUSAND = 10000;
	
	/**
	 * 用于随机万分率计算使用
	 */
	short TEN_THOUSAND_ONE = 10001;
	
	/**
	 * 用于百分比计算使用
	 */
	short HUNDRED_PERCENT = 100;

	/**
	 * 取名长度
	 */
	short MAX_NAME_LENGTH = 14;
	
	/**
	 * 房间的最大容量
	 */
	short ROOM_MAX_CAPACITY = 10;
	
	/**
	 * 邮件的最大数量
	 */
	short MAX_MAIL_COUNT = 50;
	
	/**
	 * 公会公告的最大长度
	 */
	short MAX_GUILD_SLOGAN_LEN = 50;
	
	/**
	 * 公会名字的最大长度
	 */
	short MAX_GUILDNAME_LEN = 20;
	
	/**
	 * 公会的初始人数
	 */
	int GUILD_BASE_MEMBER_COUNT = 9;
	
	/**
	 * 公会的最高等级
	 */
	int MAX_GUILD_LEVEL = 30;
	
	/**
	 * 聊天时间间隔
	 */
	int CHAT_TIME = 5;
	
	/**
	 * 队伍的最大人数
	 */
	int MAX_TEAM_MEMBER_COUNT = 5;
	
	/**
	 * 装备卡槽的个数
	 */
	int EQUIP_HOLE_COUNT = 3;
	
	/**
	 * 装备进阶最大的材料个数
	 */
	int MAX_EQUIP_ADVANCE_MATERIAL_COUNT = 10;
	
	/**
	 * 装备的属性总数
	 */
	short FIGHT_ATTRIBUTE_COUNT = 5; 
}