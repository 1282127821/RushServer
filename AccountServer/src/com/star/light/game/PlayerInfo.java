package com.star.light.game;

import com.star.light.game.DBObject;

/**
 * 玩家详细信息
 */
public class PlayerInfo extends DBObject {
	/**
	 * 玩家的Id
	 */
	private long userId;

	/**
	 * 账号Id
	 */
	private long accountId;

	/**
	 * 玩家的名字
	 */
	private String userName;

	/**
	 * 玩家的职业Id
	 */
	private int jobId;
	
	/**
	 * 玩家的等级
	 */
	private int playerLv;

	/**
	 * 玩家创建角色时间
	 */
	private int createTime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if (this.userName == null || !this.userName.equals(userName)) {
			this.userName = userName;
		}
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getPlayerLv() {
		return playerLv;
	}

	public void setPlayerLv(int playerLv) {
		this.playerLv = playerLv;
	}

	public int getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId:		").append(userId).append("\n");
		sb.append("userName:	").append(userName).append("\n");
		sb.append("jobId:	").append(jobId).append("\n");
		sb.append("level:		").append(playerLv).append("\n");
		sb.append("createTime:	").append(createTime).append("\n");
		return sb.toString();
	}
}
