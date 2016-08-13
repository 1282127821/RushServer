package com.star.light.table;

import java.util.List;

public class RewardInfo {
	/**
	 * 资源奖励列表
	 */
	private List<ResourceInfo> resourceRewardList;

	/**
	 * 奖励模式
	 */
	private int rewardType;

	/**
	 * 奖励道具列表（副本结算之后给)
	 */
	private int[] rewardList;

	/**
	 * 概率
	 */
	private int[] chance;

	/**
	 * 数量
	 */
	private int[] rewardCount;

	public List<ResourceInfo> getResourceRewardList() {
		return resourceRewardList;
	}

	public void setResourceRewardList(List<ResourceInfo> resourceRewardList) {
		this.resourceRewardList = resourceRewardList;
	}
	
	public int getRewardType() {
		return rewardType;
	}

	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}

	public int[] getRewardList() {
		return rewardList;
	}

	public void setRewardList(int[] rewardList) {
		this.rewardList = rewardList;
	}

	public int[] getChance() {
		return chance;
	}

	public void setChance(int[] chance) {
		this.chance = chance;
	}

	public int[] getRewardCount() {
		return rewardCount;
	}

	public void setRewardCount(int[] rewardCount) {
		this.rewardCount = rewardCount;
	}
}
