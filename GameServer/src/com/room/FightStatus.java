package com.room;

/**
 * 战斗状态
 */
public interface FightStatus {
	/**
	 * 无
	 */
	short NONE = 0;

	/**
	 *	已经准备好
	 */
	short READY = 1;

	/**
	 * 卡牌
	 */
	short CARD = 2;

	/**
	 * 多人副本
	 */
	short MULINSTANCE = 3;

	/**
	 * 怪物攻城
	 */
	short MONSTERSIEGE = 4;

	/**
	 * 排位赛
	 */
	short RANKPVP = 5;

	/**
	 * 世界boss
	 */
	short WORLDBOSS = 6;

	/**
	 * 多人PvP
	 */
	short MULPVP = 7;

	/**
	 * 单人本
	 */
	short CAMPAIGN = 8;

	/**
	 * 时空裂隙
	 */
	short CRANNY = 9;
	
	/**
	 * 公会守卫战
	 */
	short GUARDBATTLE = 10;

}
