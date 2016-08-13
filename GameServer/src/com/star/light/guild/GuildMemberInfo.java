package com.star.light.guild;

import com.star.light.db.DBObject;
import com.star.light.db.DBOption;

public class GuildMemberInfo extends DBObject {
	/**
	 * 角色Id
	 */
	private long userId;

	/**
	 * 角色名字
	 */
	private String userName;

	/**
	 * 角色VIP等级
	 */
	private int vipLv;

	/**
	 * 玩家的职业Id
	 */
	private int jobId;

	/**
	 * 角色在公会中的职位
	 */
	private int power;

	/**
	 * 角色的等级
	 */
	private int playerLv;

	/**
	 * 玩家战斗力
	 */
	private int fightStrength;

	/**
	 * 角色的贡献度
	 */
	private int contribution;

	/**
	 * 玩家登出时间，玩家登录进游戏则设置为0，登出游戏之后设置为离开游戏时间
	 */
	private int logoutTime;

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

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		if (this.power != power) {
			this.power = power;
			setOp(DBOption.UPDATE);
		}
	}

	public int getPlayerLv() {
		return playerLv;
	}

	public void setPlayerLv(int playerLv) {
		this.playerLv = playerLv;
	}

	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		if (this.contribution != contribution) {
			this.contribution = contribution;
			setOp(DBOption.UPDATE);
		}
	}

	public int getFightStrength() {
		return fightStrength;
	}

	public void setFightStrength(int fightStrength) {
		this.fightStrength = fightStrength;
	}

	public int getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(int logoutTime) {
		this.logoutTime = logoutTime;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId:		").append(userId).append("\n");
		sb.append("power:		").append(power).append("\n");
		sb.append("contribution:		").append(contribution).append("\n");
		return sb.toString();
	}
}
