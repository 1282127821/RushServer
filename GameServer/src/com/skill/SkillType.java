package com.skill;

/**
 * 技能类型
 */
public interface SkillType {
	/**
	 * 其他类型
	 */
	int OTHER = 0;
	
	/**
	 * 普通技能
	 */
	int NORMAL = 1;
	
	/**
	 * 0阶可升级技能
	 */
	int LEVELUP_ZERO = 2;
	
	/**
	 * 1阶可升级技能
	 */
	int LEVELUP_ONE = 3;
	
	/**
	 * 2阶可升级技能
	 */
	int LEVELUP_TWO = 4;
	
	/**
	 * 3阶可升级技能
	 */
	int LEVELUP_THREE = 5;
}
