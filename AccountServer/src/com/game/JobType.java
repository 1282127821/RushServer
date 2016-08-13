package com.game;

/**
 * 职业类型
 */
public interface JobType {
	//下面的枚举1-4必须要连续，读取技能那里判断有依赖
	/**
	 * 男主角
	 */
	short MAN = 1;

	/**
	 * 女机器人
	 */
	short ROBOT = 2;
	
	/**
	 * 男石头人
	 */
	short STONE = 3;

	/**
	 * 小萝莉
	 */
	short LOLI = 4;
}
