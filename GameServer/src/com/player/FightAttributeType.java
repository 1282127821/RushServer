package com.player;

public interface FightAttributeType {
	/**
	 * 力量
	 */
	short STRENGTH = 0;
	
	/**
	 * 敏捷
	 */
	short AGILITY = 1;
	
	/**
	 * 体质
	 */
	short VITALITY = 2;
	
	/**
	 * 智力
	 */
	short INTELLIGENCE = 3;
	
	/**
	 * 灵巧
	 */
	short DEXTERITY = 4;
	
	/**
	 * 幸运
	 */
	short LUCKY = 5;

	/**
	 * 攻击
	 */
	short ATTACK = 6;
	
	/**
	 * 防御
	 */
	short DEFENCE = 7;
	
	/**
	 * 法术攻击
	 */
	short MAGIC_ATTACK = 8;
	
	/**
	 * 法术防御
	 */
	short MAGIC_DEFENCE = 9;
	
	/**
	 * 攻速
	 */
	short ATTACK_SPEED = 10;
	
	/**
	 * 施法
	 */
	short MAGIC_COLD = 11;
	
	/**
	 * 命中
	 */
	short HIT = 12;
	
	/**
	 * 闪避
	 */
	short DODGE = 13;
	
	/**
	 * 生命
	 */
	short HP = 14;
	
	/**
	 * 法力
	 */
	short MP = 15;

	/**
	 * 用于计算个数使用，上面增加了类型之后必须更新该数值
	 */
	short COUNT = 16;
}
