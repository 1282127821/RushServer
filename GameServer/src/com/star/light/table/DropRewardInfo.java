package com.star.light.table;

public class DropRewardInfo {
	/**
	 * 掉落Id
	 */
	public int dropId;
	
	/**
	 * 奖励物品Id组
	 */
	public int[] aryDropItem;

	/**
	 * 奖励物品数量
	 */
	public int[] aryDropItemNum;

	/**
	 * 奖励物品掉落概率
	 */
	public int[] aryItemDropChance;

	/**
	 * 一轮数量上限
	 */
	public int[] aryItemNumLimit;
}
