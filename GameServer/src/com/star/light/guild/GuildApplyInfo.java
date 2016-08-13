package com.star.light.guild;

import com.star.light.db.DBObject;

public class GuildApplyInfo extends DBObject {
	/**
	 * 玩家Id
	 */
	private long userId;
	
	/**
	 * 玩家名字
	 */
	private String userName;
	
	/**
	 * 玩家等级
	 */
	private int playerLv;
	
	/**
	 * 玩家的职业Id
	 */
	private int jobId;
	
	/**
	 * 战斗力
	 */
	private int fightStrength;
	
	/**
	 * 玩家VIP等级
	 */
	private int vipLv;
	
	/**
	 * 申请时间
	 */
	private int applyTime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPlayerLv() {
		return playerLv;
	}

	public void setPlayerLv(int playerLv) {
		this.playerLv = playerLv;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getFightStrength() {
		return fightStrength;
	}

	public void setFightStrength(int fightStrength) {
		this.fightStrength = fightStrength;
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}
	
	public int getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(int applyTime) {
		this.applyTime = applyTime;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId:		").append(userId).append("\n");
		sb.append("applyTime:		").append(applyTime).append("\n");
		return sb.toString();
	}
}
