package com.game;

/**
 * 玩家详细信息
 */
public class PlayerInfo
{
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
	 * 玩家的职业类型
	 */
	private int jobType;

	/**
	 * 玩家的等级
	 */
	private int playerLv;

	/**
	 * 玩家创建角色时间
	 */
	private int createTime;

	/**
	 * 战斗力
	 */
	private int fightStrength;

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public long getAccountId()
	{
		return accountId;
	}

	public void setAccountId(long accountId)
	{
		this.accountId = accountId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		if (this.userName == null || !this.userName.equals(userName))
		{
			this.userName = userName;
		}
	}

	public int getJobType()
	{
		return jobType;
	}

	public void setJobType(int jobType)
	{
		this.jobType = jobType;
	}

	public int getPlayerLv()
	{
		return playerLv;
	}

	public void setPlayerLv(int playerLv)
	{
		this.playerLv = playerLv;
	}

	public int getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(int createTime)
	{
		this.createTime = createTime;
	}

	public int getFightStrength()
	{
		return fightStrength;
	}

	public void setFightStrength(int fightStrength)
	{
		this.fightStrength = fightStrength;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("userId:		").append(userId).append("\n");
		sb.append("userName:	").append(userName).append("\n");
		sb.append("jobType:	").append(jobType).append("\n");
		sb.append("level:		").append(playerLv).append("\n");
		sb.append("createTime:	").append(createTime).append("\n");
		sb.append("fightStrength:	").append(fightStrength).append("\n");
		return sb.toString();
	}
}
