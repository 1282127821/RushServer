package com.guild;

import com.db.DBObject;
import com.db.DBOption;

public class GuildInfo extends DBObject {
	/**
	 * 公会Id
	 */
	private long guildId;

	/**
	 * 公会名字
	 */
	private String guildName;

	/**
	 * 公会公告
	 */
	private String guildSlogan;

	/**
	 * 公会会徽
	 */
	private short guildEmblem;
	
	/**
	 * 公会的等级
	 */
	private short guildLv;

	/**
	 * 公会总经验值
	 */
	private int totalExp;

	/**
	 * 创建时间
	 */
	private int createTime;

	/**
	 * 标记是否存在，主要用于日志统计那边使用，解散公会是标记为false
	 */
	private boolean isExist;

	/**
	 * 是否需要审核加入公会
	 */
	private boolean isAudit;

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public String getGuildSlogan() {
		return guildSlogan;
	}

	public void setGuildSlogan(String guildSlogan) {
		if (this.guildSlogan == null || !this.guildSlogan.equals(guildSlogan)) {
			this.guildSlogan = guildSlogan;
			setOp(DBOption.UPDATE);
		}
	}

	public short getGuildEmblem() {
		return guildEmblem;
	}

	public void setGuildEmblem(short guildEmblem) {
		this.guildEmblem = guildEmblem;
	}

	public short getGuildLv() {
		return guildLv;
	}

	public void setGuildLv(short guildLv) {
		this.guildLv = guildLv;
		setOp(DBOption.UPDATE);
	}
	
	public int getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(int totalExp) {
		if (this.totalExp != totalExp) {
			this.totalExp = totalExp;
			setOp(DBOption.UPDATE);
		}
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public boolean isExist() {
		return this.isExist;
	}

	public void setExist(boolean isExist) {
		if (this.isExist != isExist) {
			this.isExist = isExist;
			setOp(DBOption.UPDATE);
		}
	}
	
	public boolean getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(boolean isAudit) {
		if (this.isAudit != isAudit) {
			this.isAudit = isAudit;
			setOp(DBOption.UPDATE);
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("guildId:		").append(guildId).append("\n");
		sb.append("guildName:		").append(guildName).append("\n");
		sb.append("guildSlogan:		").append(guildSlogan).append("\n");
		sb.append("guildEmblem:		").append(guildEmblem).append("\n");
		sb.append("guildLv:		").append(guildLv).append("\n");
		sb.append("totalExp:		").append(totalExp).append("\n");
		sb.append("createTime:		").append(createTime).append("\n");
		sb.append("isExist:		").append(isExist).append("\n");
		sb.append("isAudit:		").append(isAudit).append("\n");
		return sb.toString();
	}
}
