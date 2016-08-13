package com.star.light.table;

public class LevelStageInfo {
	/**
	 * 关卡Id
	 */
	public int stageId;

	/**
	 * 关卡类型 1 普通 2精英
	 */
	public int stageType;

	/**
	 * 关卡获得的经验值
	 */
	public int rewardExp;

	/**
	 * 关卡获得的金钱
	 */
	public int rewardMoney;

	/**
	 * 关卡的掉落Id
	 */
	public int rewardDropId;
	
	/**
	 * 关卡的怪物Id列表
	 */
	public int[] monsterId;
	
	/**
	 * 关卡的怪物Node Id列表
	 */
	public int[] monsterNodeId;
}