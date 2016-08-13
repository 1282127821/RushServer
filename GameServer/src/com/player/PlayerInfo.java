package com.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.db.DBObject;
import com.db.DBOption;

/**
 * 玩家详细信息
 */
public class PlayerInfo extends DBObject {
	//TODO:LZGLZG这个可以考虑二进制的位标识来标记更新
	/**
	 * 玩家属性
	 */
	HashSet<Short> synchSet = new HashSet<Short>();

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
	 * 玩家钻石
	 */
	private int diamond;

	/**
	 * 玩家金币
	 */
	private int gold;

	/**
	 * 玩家的职业Id
	 */
	private int jobId;

	/**
	 * 玩家的状态
	 */
	private int playerState;
	
	/**
	 * 新手引导进度
	 */
	private int noviceProcess;
	
	/**
	 * 新手引导步骤
	 */
	private int noviceStep;
	
	/**
	 * 玩家的等级
	 */
	private int playerLv;
	
	/**
	 * 玩家的战斗力
	 */
	private int fightStrength;
	
	/**
	 * 玩家经验
	 */
	private int playerExp;
	
	/**
	 * 玩家的当前血量
	 */
	private int currHP;
	
	/**
	 * 玩家的最大血量
	 */
	private int maxHP;
	
	/**
	 * 玩家的当前法术值
	 */
	private int currMP;
	
	/**
	 * 玩家的最大法术值
	 */
	private int maxMP;

	/**
	 * 玩家所在的X位置
	 */
	private float posX;
	
	/**
	 * 玩家所在的Y位置
	 */
	private float posY;
	
	/**
	 * 玩家所在的Z位置
	 */
	private float posZ;

	/**
	 * 玩家的方向
	 */
	private float direct;

	/**
	 * 玩家所在的公会Id
	 */
	private long guildId;

	/**
	 * 玩家创建角色时间
	 */
	private int createTime;

	/**
	 * 玩家登陆游戏的时间
	 */
	private int loginTime;

	/**
	 * 玩家登出游戏时间
	 */
	private int logoutTime;
	
	/**
	 * 玩家的重置时间
	 */
	private int resetTime;
	
	/**
	 * 玩家的VIP等级
	 */
	private int vipLv;
	
	/**
	 * 玩家的黑名单列表
	 */
	private List<String> blackUserList = new ArrayList<String>();

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
			setOp(DBOption.UPDATE);
			synchSet.add(PlayerSynchType.USERNAME);
		}
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.GOLD);
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.JOB_ID);
	}

	public int getPlayerState() {
		return playerState;
	}

	public void setPlayerState(int playerState) {
		this.playerState = playerState;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.STATE);
	}

	public int getNoviceProcess() {
		return noviceProcess;
	}

	public void setNoviceProcess(int noviceProcess) {
		this.noviceProcess = noviceProcess;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.NOVICE_PROCESS);
	}

	public int getNoviceStep() {
		return noviceStep;
	}

	public void setNoviceStep(int noviceStep) {
		this.noviceStep = noviceStep;
		setOp(DBOption.UPDATE);
	}

	public int getPlayerLv() {
		return playerLv;
	}

	public void setPlayerLv(int playerLv) {
		this.playerLv = playerLv;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.LEVEL);
	}

	public int getPlayerExp() {
		return playerExp;
	}

	public void setPlayerExp(int playerExp) {
		this.playerExp = playerExp;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.EXP);
	}

	public int getCurrHP() {
		return currHP;
	}

	public void setCurrHP(int currHP) {
		this.currHP = currHP;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.CURR_HP);
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
		synchSet.add(PlayerSynchType.MAX_HP);
	}
	
	public int getCurrMP() {
		return currMP;
	}

	public void setCurrMP(int currMP) {
		this.currMP = currMP;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.CURR_MP);
	}

	public int getMaxMP() {
		return maxMP;
	}

	public void setMaxMP(int maxMP) {
		this.maxMP = maxMP;
		synchSet.add(PlayerSynchType.MAX_MP);
	}
	
	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.POS_X);
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.POS_Y);
	}

	public float getDirect() {
		return direct;
	}

	public void setDirect(float direct) {
		this.direct = direct;
		setOp(DBOption.UPDATE);
		synchSet.add(PlayerSynchType.DIRECT);
	}

	public float getPosZ() {
		return posZ;
	}

	public void setPosZ(float posZ) {
		this.posZ = posZ;
		setOp(DBOption.UPDATE);
	}
	
	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		if (this.guildId != guildId) {
			this.guildId = guildId;
			setOp(DBOption.UPDATE);
			synchSet.add(PlayerSynchType.GUILD_ID);
		}
	}

	public int getFightStrength() {
		return fightStrength;
	}

	public void setFightStrength(int fightStrength) {
		if (this.fightStrength != fightStrength) {
			this.fightStrength = fightStrength;
			setOp(DBOption.UPDATE);
			synchSet.add(PlayerSynchType.FIGHT_STRENGTH);
		}
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		if (this.diamond != diamond) {
			this.diamond = diamond;
			setOp(DBOption.UPDATE);
			synchSet.add(PlayerSynchType.DIAMOND);
		}
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public int getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(int loginTime) {
		if (this.loginTime != loginTime) {
			this.loginTime = loginTime;
			setOp(DBOption.UPDATE);
		}
	}

	public int getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(int logoutTime) {
		if (this.logoutTime != logoutTime) {
			this.logoutTime = logoutTime;
			setOp(DBOption.UPDATE);
		}
	}

	public int getResetTime() {
		return resetTime;
	}

	public void setResetTime(int resetTime) {
		if (this.resetTime != resetTime) {
			this.resetTime = resetTime;
			setOp(DBOption.UPDATE);
		}
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		if (this.vipLv != vipLv) {
			this.vipLv = vipLv;
			setOp(DBOption.UPDATE);
		}
	}
	
	public String getStrBlackUser() {
		StringBuilder strBlackUser = new StringBuilder();
		for (String userName: blackUserList) {
			strBlackUser.append(userName).append(",");
		}
		
		return strBlackUser.toString();
	}
	
	public void setStrBlackUser(String strBlackUser) {
		if (!strBlackUser.equals("")) {
			String[] aryBlackUser = strBlackUser.split(",");
			for (int i = 0; i < aryBlackUser.length; i++) {
				blackUserList.add(aryBlackUser[i]);
			}
		}
	}
	
	public List<String> getBlackUserList() {
		return blackUserList;
	}

	public void setBlackUserList(List<String> blackUserList) {
		this.blackUserList = blackUserList;
		setOp(DBOption.UPDATE);
	}
	
	public HashSet<Short> getSynchSet() {
		return synchSet;
	}

	public void setSynchSet(HashSet<Short> synchSet) {
		this.synchSet = synchSet;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("userId:		").append(userId).append("\n");
		sb.append("userName:	").append(userName).append("\n");
		sb.append("diamond:		").append(diamond).append("\n");
		sb.append("gold:		").append(gold).append("\n");
		sb.append("jobId:	").append(jobId).append("\n");
		sb.append("state:		").append(playerState).append("\n");
		sb.append("noviceProcess:").append(noviceProcess).append("\n");
		sb.append("level:		").append(playerLv).append("\n");
		sb.append("fightStrength:").append(fightStrength).append("\n");
		sb.append("exp:			").append(playerExp).append("\n");
		sb.append("guildId:		").append(guildId).append("\n");
		sb.append("createTime:	").append(createTime).append("\n");
		sb.append("loginTime:	").append(loginTime).append("\n");
		sb.append("logoutTime:	").append(logoutTime).append("\n");
		sb.append("resetTime:	").append(resetTime).append("\n");
		return sb.toString();
	}

	/**
	 * 检查属性是否修改过
	 */
	public boolean isChange(short synchType) {
		return synchSet.contains(synchType);
	}

	public void clearSynchSet() {
		synchSet.clear();
	}
}
