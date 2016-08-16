package com.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import com.GameServerHandler;
import com.db.DBOption;
import com.friend.FriendMgr;
import com.google.protobuf.AbstractMessage.Builder;
import com.guild.Guild;
import com.guild.GuildMgr;
import com.mail.MailMgr;
import com.mina.LinkedClient;
import com.netmsg.AbstractCmdTaskQueue;
import com.netmsg.CmdTask;
import com.netmsg.CmdTaskQueue;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.BlackInfoListMsg;
import com.pbmessage.GamePBMsg.DiceInfoMsg;
import com.pbmessage.GamePBMsg.PVPRandomDiceMsg;
import com.pbmessage.GamePBMsg.PlayerMsg;
import com.pbmessage.GamePBMsg.PvpBattleResultMsg;
import com.pbmessage.GamePBMsg.TipInfoMsg;
import com.prop.BagType;
import com.prop.PlayerPropMgr;
import com.prop.Prop;
import com.prop.PropTemplate;
import com.prop.PropType;
import com.protocol.Protocol;
import com.room.FightStatus;
import com.room.Room;
import com.scene.SceneType;
import com.skill.FightSkillMgr;
import com.table.CharacterInfo;
import com.table.CharacterInfoMgr;
import com.table.ConfigMgr;
import com.table.DropRewardInfo;
import com.table.ExpInfo;
import com.table.ExpInfoMgr;
import com.table.ItemTemplateMgr;
import com.table.LevelAttributeInfo;
import com.table.LevelAttributeInfoMgr;
import com.table.LevelStageInfo;
import com.table.LevelStageInfoMgr;
import com.table.LockData;
import com.table.ResourceInfo;
import com.table.RewardInfoMgr;
import com.team.Team;
import com.util.GameLog;
import com.util.TimeUtil;

public class GamePlayer {
	/**
	 * 当前的任务Id
	 */
	public int curTaskId;

	/**
	 * 玩家之前的场景类型
	 */
	public int beforeSceneType;

	/**
	 * 场景类型
	 */
	public int sceneType;

	/**
	 * PVP状态的类型
	 */
	private short fightStatus = FightStatus.NONE;

	/**
	 * 更新玩家属性的个数
	 */
	public AtomicInteger changeCount;

	/**
	 * 玩家的基本个人数据
	 */
	public PlayerInfo playerInfo;

	/**
	 * 网关链接
	 */
	public LinkedClient gateWayclient;

	/**
	 * 聊天时间
	 */
	public int chatTime;

	private CmdTaskQueue cmdTaskQueue;
	private Object lock;
	private LockData loadLock = new LockData();
	private Room room;
	private Team team;
	private FightInventory fightInventory;
	private PlayerPropMgr propMgr;
	private FightSkillMgr fightSkillMgr;
	private FriendMgr friendMgr;
	private MailMgr mailMgr;

	private List<PropertyInfo> randomItemIdList;
	private int[] aryWhite = new int[] { 1, 0, 0, 0, 0, 1 };
	private int[] aryBlack = new int[] { 1, 0, 0, 0, 1, 1 };
	private int[] aryGreen = new int[] { 1, 1, 1, 1, 1, 1 };
	private int[] aryGold = new int[] { 1, 0, 0, 1, 1, 1 };

	public GamePlayer() {
		cmdTaskQueue = new AbstractCmdTaskQueue(GameServerHandler.executor);
		changeCount = new AtomicInteger(0);
	}

	public boolean isSyncScene() {
		return sceneType == SceneType.MAIN_CITY;
	}

	public long getUserId() {
		return playerInfo.getUserId();
	}

	public String getUserName() {
		return playerInfo.getUserName();
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public long getTeamId() {
		return team != null ? team.getTeamId() : 0;
	}

	/**
	 * 获取玩家的等级
	 */
	public int getPlayerLv() {
		return playerInfo.getPlayerLv();
	}

	public long getGuildId() {
		return playerInfo.getGuildId();
	}

	public void setGuildId(long guildId) {
		playerInfo.setGuildId(guildId);
		syncPlayerProperty(false);
	}

	public boolean isOpenGuild() {
		return playerInfo.getPlayerLv() >= ConfigMgr.openGuildLv;
	}

	/**
	 * 获取用户状态
	 */
	public int getPlayerState() {
		return playerInfo.getPlayerState();
	}

	/**
	 * 判断玩家是否在线
	 */
	public boolean isOnline() {
		return getPlayerState() == PlayerState.ONLINE;
	}

	/**
	 * 获取用户战斗力
	 */
	public int getFightStrength() {
		return playerInfo.getFightStrength();
	}

	/**
	 * 获取用户登陆时间
	 */
	public int getLoginTime() {
		return playerInfo.getLoginTime();
	}

	public short getFightStatus() {
		return fightStatus;
	}

	public void setFightStatus(short fightStatus) {
		this.fightStatus = fightStatus;
	}

	public void addBlackUser(String userName) {
		List<String> blackUser = playerInfo.getBlackUserList();
		blackUser.add(userName);
		playerInfo.setBlackUserList(blackUser);
	}

	public void deleteBlackUser(String userName) {
		List<String> blackUser = playerInfo.getBlackUserList();
		blackUser.remove(userName);
		playerInfo.setBlackUserList(blackUser);
	}

	/**
	 * 发送黑名单列表给玩家
	 */
	public void sendTotalBlackUser() {
		BlackInfoListMsg.Builder netMsg = BlackInfoListMsg.newBuilder();
		List<String> blackUser = playerInfo.getBlackUserList();
		for (String userName : blackUser) {
			netMsg.addBlackInfoList(userName);
		}

		sendPacket(Protocol.S_C_TOTAL_BLACK_USER, netMsg);
	}

	/**
	 * 根据名字判断该玩家是否在黑名单中
	 */
	public boolean isBlackUser(String userName) {
		List<String> blackUser = playerInfo.getBlackUserList();
		for (String name : blackUser) {
			if (name.equals(userName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 添加属性给玩家
	 */
	public void addAttributeToPlayer() {
		Prop[] aryEquip = propMgr.getAllProp(BagType.EQUIP_FENCE);
		for (int index = 0, len = aryEquip.length; index < len; index++) {
			Prop equip = aryEquip[index];
			if (equip != null) {
				addEquipAttribute(index, false);
			}
		}

		CharacterInfo charInfo = CharacterInfoMgr.getInstance().getCharacterInfo(playerInfo.getJobId());
		if (charInfo != null) {
			fightInventory.addCharacterHP(charInfo.charHP);
		}

		addLevelAttribute(false);
		// 必须保证在最后
		fightInventory.syncFightAttribute();
	}

	/**
	 * 给玩家增加等级的属性加成
	 */
	public void addLevelAttribute(boolean sendClient) {
		LevelAttributeInfo levelAttributeInfo = LevelAttributeInfoMgr.getInstance()
				.getLevelAttributeInfo(playerInfo.getJobId(), getPlayerLv());
		if (levelAttributeInfo != null) {
			fightInventory.addLevelAttribute(levelAttributeInfo.attributeValue);
			if (sendClient) {
				fightInventory.syncFightAttribute();
			}
		}
	}

	/**
	 * 增加装备属性给玩家
	 */
	public void addEquipAttribute(int posIndex, boolean isSyncClient) {
		Prop equip = propMgr.getPropByPosIndex(BagType.EQUIP_FENCE, posIndex);
		fightInventory.clearEquipAttribute(posIndex);
		if (equip != null) {
			int[] aryEquipAttribute = new int[FightAttributeType.COUNT];
			PropertyInfo[] aryAttributeValue = equip.getItemTempInfo().aryAttributeValue;
			for (int i = 0, len = aryAttributeValue.length; i < len; i++) {
				PropertyInfo propertyInfo = aryAttributeValue[i];
				if (propertyInfo.type > 0) {
					aryEquipAttribute[propertyInfo.type] = propertyInfo.value;
				}
			}
			
			//祝福的属性
			aryEquipAttribute[aryAttributeValue[0].type] += equip.getBlessAttribute();
			
			//增加镶嵌的属性
			int[] aryInlay = equip.getPropInstance().getAryInlay();
			for (int i = 0, len = aryInlay.length; i < len; ++i) {
				int inlayCardId = aryInlay[i];
				if (inlayCardId > 0) {
					PropTemplate cardProp = ItemTemplateMgr.getInstance().getItemTempInfo(inlayCardId);
					PropertyInfo propertyInfo = cardProp.aryAttributeValue[0];
					aryEquipAttribute[propertyInfo.type] = propertyInfo.value;
				}
			}

			fightInventory.addEquipAttribute(posIndex, aryEquipAttribute);
		}

		if (isSyncClient) {
			fightInventory.syncFightAttribute();
		}
	}

	public CmdTaskQueue getCmdTaskQueue() {
		return cmdTaskQueue;
	}

	public void enqueue(NetCmd netCmd, PBMessage packet) {
		cmdTaskQueue.enqueue(new CmdTask(this, netCmd, packet, cmdTaskQueue));
	}

	public FightInventory getFightInventory() {
		return fightInventory;
	}

	public PlayerPropMgr getPropMgr() {
		return propMgr;
	}

	public FightSkillMgr getSkillInventory() {
		return fightSkillMgr;
	}

	public FriendMgr getFriendMgr() {
		return friendMgr;
	}

	public MailMgr getMailMgr() {
		return mailMgr;
	}

	public int getJobId() {
		return playerInfo.getJobId();
	}

	/**
	 * 获取玩家的Vip等级
	 */
	public int getVipLv() {
		return playerInfo.getVipLv();
	}

	/**
	 * 同步公会的成员的信息
	 */
	public void syncGuildMemInfo(short propertyType, int propertyValue) {
		long guildId = getGuildId();
		if (guildId > 0) {
			GuildMgr.getInstance().syncGuildMemInfo(guildId, getUserId(), propertyType, propertyValue);
		}
	}

	/**
	 * 加载共享内存
	 */
	public boolean loadShareData(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
		lock = new Object();
		return true;
	}

	public void unLoadShareData() {

	}

	/**
	 * 加载用户私有数据
	 */
	public boolean loadPersonData() {
		synchronized (lock) {
			if (getPlayerState() != PlayerState.OFFLINE) {
				return false;
			}
		}
		setPlayerState(PlayerState.ONLINE);
		WorldMgr.onlineCount.incrementAndGet();
		if (loadLock.beginLock()) {
			try {
				fightInventory = new FightInventory(this);
				checkLevel();

				if (playerInfo.getDiamond() <= 0) {
					addDiamond(10000, ItemChangeType.GMGET);
					addGold(100000, ItemChangeType.GMGET);
				}

				propMgr = new PlayerPropMgr(this);
				propMgr.loadFromDB();

				fightSkillMgr = new FightSkillMgr(this);
				fightSkillMgr.loadFromDB();

				friendMgr = new FriendMgr(this);
				friendMgr.loadFromDB();

				mailMgr = new MailMgr(this);
				mailMgr.loadFromDB();

				syncGuildMemInfo(PlayerSynchType.LOGOUT_TIME, 0);
				// 必须保证在最后面，因为是统一计算玩家的属性和战斗力
				addAttributeToPlayer();
				save();
			} catch (Exception e) {
				setPlayerState(PlayerState.OFFLINE);
				GameLog.error(String.format("用户 userId = %s, nickName = %s:私有数据加载失败", getUserId(), getUserName()), e);
				return false;
			} finally {
				loadLock.commitLock();
			}
			return true;
		}
		return false;
	}

	/**
	 * 从内存中把玩家的数据卸载掉
	 */
	public boolean unLoadPersonData() {
		if (loadLock.beginLock()) {
			try {
				setPlayerState(PlayerState.OFFLINE);
				WorldMgr.onlineCount.decrementAndGet();
				int logoutTime = TimeUtil.getSysCurSeconds();
				setLogoutTime(logoutTime);
				syncGuildMemInfo(PlayerSynchType.LOGOUT_TIME, logoutTime);
				unloadData();
				save();
				if (propMgr != null) {
					propMgr.unloadData();
					propMgr = null;
				}

				if (fightSkillMgr != null) {
					fightSkillMgr.unloadData();
					fightSkillMgr = null;
				}

				if (fightInventory != null) {
					fightInventory.unloadData();
					fightInventory = null;
				}

				if (friendMgr != null) {
					friendMgr.unloadData();
					friendMgr = null;
				}

				if (mailMgr != null) {
					mailMgr.unloadData();
				}
			} catch (Exception e) {
				GameLog.error("unload personal data error. userId : " + getUserId(), e);
			} finally {
				loadLock.commitLock();
			}
			return true;
		}

		return false;
	}

	/**
	 * 保存用户全部数据
	 */
	public void save() {
		try {
			if (playerInfo.getOp() == DBOption.UPDATE) {
				DaoMgr.playerInfoDao.updatePlayerInfo(playerInfo);
			}
		} catch (Exception e) {
			GameLog.error("保存玩家数据出错: UserId" + getUserId(), e);
		}

		if (propMgr != null) {
			propMgr.saveToDB();
		}

		if (fightSkillMgr != null) {
			fightSkillMgr.saveToDB(getJobId());
		}

		if (friendMgr != null) {
			friendMgr.saveToDB();
		}

		if (mailMgr != null) {
			mailMgr.saveToDB();
		}
	}

	/**
	 * 发送消息包
	 */
	public void sendPacket(short msgId, Builder<?> msgBuilder) {
		PBMessage packet = new PBMessage(msgId);
		if (msgBuilder != null) {
			packet.setMessage(msgBuilder.build());
		}

		sendPacket(packet);
	}

	/**
	 * 发送消息包
	 */
	public void sendPacket(PBMessage packet) {
		if (gateWayclient != null) {
			packet.setUserId(playerInfo.getUserId());
			gateWayclient.send(packet);
		} else {
			GameLog.error("Can not found gateway connection , userId = " + getUserId() + " packet forward failed.");
		}
	}

	/**
	 * 发送各类提示信息给客户端
	 */
	public void sendTips(int tipId, Object... args) {
		TipInfoMsg.Builder netMsg = TipInfoMsg.newBuilder();
		netMsg.setTipId(tipId);
		for (int index = 0; index < args.length; index++) {
			netMsg.addContent(String.valueOf(args[index]));
		}

		sendPacket(Protocol.S_C_TIP_INFO, netMsg);
	}

	/**
	 * 发送错误码
	 */
	public void sendErrorCode(short code, byte errCode) {
		PBMessage pbMessage = new PBMessage(code, errCode);
		sendPacket(pbMessage);
	}

	/**
	 * 每天重置玩家数据
	 */
	public void refershData(Date currentDate) {
		try {
			int intervalDay = TimeUtil.dataCompare5(new Date(playerInfo.getResetTime() * 1000L), currentDate);
			if (intervalDay >= 1) {
				resetData();
			}
		} catch (Exception e) {
			GameLog.error("重置玩家数据异常,  userId : " + getUserId() + ", userName : " + getUserName(), e);
		}
	}

	/**
	 * 每天固定时间重置玩家数据
	 */
	public void resetData() {
		try {
			beginChanges();
			playerInfo.setResetTime(TimeUtil.getSysCurSeconds());
		} catch (Exception e) {
			String msg = "5点重置数据,userId :  " + getUserId() + ", userName : " + getUserName() + " , resetTime : "
					+ TimeUtil.getDateFormat(new Date(playerInfo.getResetTime() * 1000L));
			GameLog.error(msg, e);
		} finally {
			commitChages(true);
		}
	}

	/**
	 * 提交打开
	 */
	public void beginChanges() {
		changeCount.incrementAndGet();
	}

	/**
	 * 提交数据
	 */
	public void commitChages(boolean isAllSynch) {
		int changes = changeCount.decrementAndGet();
		if (changes < 0) {
			GameLog.error("changeCount Inventory changes counter is bellow zero (forgot to use BeginChanges?)!\n\n");
			changeCount.set(0);
		}

		if (changes <= 0) {
			sendPlayerInfo(isAllSynch);
		}
	}

	public void sendPlayerInfo(boolean isAllSynch) {
		PlayerMsg.Builder playerInfoMsg = PlayerMsg.newBuilder();
		initPlayerMsg(playerInfoMsg, isAllSynch);
		sendPacket(Protocol.S_C_PLAYER_INFO, playerInfoMsg);
	}

	/**
	 * 设置用户状态
	 */
	public void setPlayerState(short playerState) {
		playerInfo.setPlayerState(playerState);
	}

	/**
	 * 获得玩家登出游戏时间
	 */
	public int getLogoutTime() {
		return playerInfo.getLogoutTime();
	}

	/**
	 * 设置玩家登出游戏时间
	 */
	public void setLogoutTime(int logoutTime) {
		playerInfo.setLogoutTime(logoutTime);
	}

	/**
	 * 设置玩家登录游戏时间
	 */
	public void setLoginTime(int loginTime) {
		playerInfo.setLoginTime(loginTime);
	}

	private void setPlayerLv(int playerLv) {
		playerInfo.setPlayerLv(playerLv);
		syncPlayerProperty(false);
	}

	/**
	 * 获取玩家所拥有的钻石
	 */
	public int getDiamond() {
		return playerInfo.getDiamond();
	}

	/**
	 * 增加钻石
	 */
	public boolean addDiamond(int value, short changeType) {
		boolean isSuccess = false;
		int srcDiamond = 0;
		synchronized (lock) {
			if (value > 0) {
				srcDiamond = playerInfo.getDiamond();
				playerInfo.setDiamond(srcDiamond + value);
				isSuccess = true;
			}
		}

		if (isSuccess) {
			syncPlayerProperty(false);
		}
		return isSuccess;
	}

	/**
	 * 移除钻石
	 */
	public boolean removeDiamond(int value, short changeType) {
		boolean isSuccess = false;
		int srcDiamond;
		synchronized (lock) {
			srcDiamond = playerInfo.getDiamond();
			if (value > 0 && value <= srcDiamond) {
				playerInfo.setDiamond(srcDiamond - value);
				isSuccess = true;
			}
		}
		if (isSuccess) {
			syncPlayerProperty(false);
		}
		return isSuccess;
	}

	/**
	 * 获取玩家所拥有的金币
	 */
	public int getGold() {
		return playerInfo.getGold();
	}

	/**
	 * 增加金币
	 */
	public int addGold(int value, short changeType) {
		int srcGold;
		synchronized (lock) {
			if (value <= 0) {
				return 0;
			}
			
			srcGold = playerInfo.getGold();
			playerInfo.setGold(srcGold + value);
		}

		syncPlayerProperty(false);
		return value;
	}

	/**
	 * 移除金币
	 */
	public boolean removeGold(int value, short changeType) {
		boolean isSuccess = false;
		int srcGold = 0;
		synchronized (lock) {
			srcGold = playerInfo.getGold();
			if (value > 0 && value <= srcGold) {
				playerInfo.setGold(srcGold - value);
				isSuccess = true;
			}
		}

		if (isSuccess) {
			syncPlayerProperty(false);
		}
		return isSuccess;
	}

	/**
	 * 添加各类资源
	 */
	public void addResource(int resourceId, int resourceCount, short diamondType, short resourceType) {
		switch (resourceId) {
		case PropType.GOLD:
			addGold(resourceCount, resourceType);
			break;

		case PropType.DIAMOND:
			addDiamond(resourceCount, diamondType);
			break;

		case PropType.EXP:
			addExp(resourceCount);
			break;

		default:
			GameLog.info(String.format("资源%s没有添加成功", resourceId));
			break;
		}
	}

	public void setVipLvl(int val) {
		playerInfo.setVipLv(val);
		syncPlayerProperty(false);
	}

	public void updateFightStrength(int fightStrength) {
		if (playerInfo.getFightStrength() != fightStrength) {
			playerInfo.setFightStrength(fightStrength);
			syncPlayerProperty(false);
		}
	}

	private void syncPlayerProperty(boolean isAllSynch) {
		if (changeCount.get() <= 0) {
			PlayerMsg.Builder playerInfoMsg = PlayerMsg.newBuilder();
			initPlayerMsg(playerInfoMsg, isAllSynch);
			sendPacket(Protocol.S_C_PLAYER_INFO, playerInfoMsg);
		}
	}

	public void initPlayerMsg(PlayerMsg.Builder playerMsg, boolean isSynch) {
		playerMsg.setUserId(playerInfo.getUserId());
		if (isSynch || playerInfo.isChange(PlayerSynchType.USERNAME)) {
			playerMsg.setUserName(playerInfo.getUserName());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.JOB_ID)) {
			playerMsg.setJobId(playerInfo.getJobId());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.GOLD)) {
			playerMsg.setGold(playerInfo.getGold());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.DIAMOND)) {
			playerMsg.setDiamond(playerInfo.getDiamond());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.STATE)) {
			playerMsg.setPlayerState(playerInfo.getPlayerState());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.LEVEL)) {
			playerMsg.setPlayerLv(playerInfo.getPlayerLv());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.EXP)) {
			playerMsg.setPlayerExp(playerInfo.getPlayerExp());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.NOVICE_PROCESS)) {
			playerMsg.setNoviceProcess(playerInfo.getNoviceProcess());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.FIGHT_STRENGTH)) {
			playerMsg.setFightStrength(playerInfo.getFightStrength());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.POS_X)) {
			playerMsg.setPosX(playerInfo.getPosX());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.POS_Y)) {
			playerMsg.setPosY(playerInfo.getPosY());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.DIRECT)) {
			playerMsg.setDirection(playerInfo.getDirect());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.CURR_HP)) {
			playerMsg.setCurrHP(playerInfo.getCurrHP());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.MAX_HP)) {
			playerMsg.setMaxHP(playerInfo.getMaxHP());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.CURR_MP)) {
			playerMsg.setCurrMP(playerInfo.getCurrMP());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.MAX_MP)) {
			playerMsg.setMaxMP(playerInfo.getMaxMP());
		}

		if (isSynch || playerInfo.isChange(PlayerSynchType.GUILD_ID)) {
			long guildId = playerInfo.getGuildId();
			playerMsg.setGuildId(guildId);
			if (guildId > 0) {
				playerMsg.setGuildName(GuildMgr.getInstance().getGuildNameById(guildId));
			}
		}

		playerInfo.clearSynchSet();
	}

	public void unloadData() {
		changeCount.set(0);
	}

	/**
	 * 给玩家增加经验，满足条件的时候会对玩家进行升级操作
	 */
	public void addExp(int exp) {
		if (playerInfo.getPlayerLv() > ConfigMgr.playerMaxLv) {
			return;
		}
		playerInfo.setPlayerExp(playerInfo.getPlayerExp() + exp);
		syncPlayerProperty(false);
		checkLevel();
	}

	/**
	 * 检测是否需要升级
	 */
	public void checkLevel() {
		int curLv = playerInfo.getPlayerLv();
		ExpInfo expInfo = ExpInfoMgr.getInstance().getExpInfo(curLv);
		if (expInfo == null) {
			return;
		}

		int nowLv = curLv;
		int leftExp = playerInfo.getPlayerExp() - expInfo.needExp;
		while (leftExp > 0) {
			nowLv += 1;
			playerInfo.setPlayerExp(leftExp);
			expInfo = ExpInfoMgr.getInstance().getExpInfo(nowLv);
			if (expInfo == null) {
				break;
			}
			leftExp = playerInfo.getPlayerExp() - expInfo.needExp;
		}

		if (curLv != nowLv) {
			setPlayerLv(nowLv);
			syncGuildMemInfo(PlayerSynchType.LEVEL, nowLv);
			addLevelAttribute(true);
		}
	}

	public void giveStageReward(int stageId, PvpBattleResultMsg.Builder netMsg) {
		LevelStageInfo stageInfo = LevelStageInfoMgr.getInstance().getLevelStageInfo(stageId);
		if (stageInfo == null) {
			return;
		}

		int oldLv = getPlayerLv();
		addExp(stageInfo.rewardExp);
		if (getPlayerLv() > oldLv) {
			netMsg.setIsLevelUp(true);
		}
		addGold(stageInfo.rewardMoney, ItemChangeType.LEVEL_STAGE_REWARD);
		netMsg.setRewardExp(stageInfo.rewardExp);
		netMsg.setRewardGold(stageInfo.rewardMoney);
		DropRewardInfo dropRewardInfo = RewardInfoMgr.getInstance().getDropRewardInfo(stageInfo.rewardDropId);
		if (dropRewardInfo == null) {
			return;
		}

		List<ResourceInfo> rewardList = new ArrayList<ResourceInfo>(3);
		List<Integer> itemList = new ArrayList<Integer>(5);
		for (int i = 0; i < 5; i++) {
			itemList.add(dropRewardInfo.aryDropItem[i]);
		}

		for (int i = 0; i < 3; i++) {
			int index = ThreadLocalRandom.current().nextInt(0, itemList.size());
			int itemId = itemList.get(index);
			rewardList.add(new ResourceInfo(itemId, 1));
			netMsg.addRewardItemIdList(itemId);
			itemList.remove(index);
			propMgr.addPropToPackageOrMail(rewardList, ItemChangeType.LEVEL_STAGE_REWARD);
		}

		List<Integer> rewardItemList = new ArrayList<Integer>(16);
		for (int i = 0; i < 16; i++) {
			rewardItemList.add(11001 + i);
		}

		randomItemIdList = new ArrayList<PropertyInfo>(8);
		for (int i = 0; i < 8; i++) {
			int index = ThreadLocalRandom.current().nextInt(0, rewardItemList.size());
			int itemId = rewardItemList.get(index);
			randomItemIdList
					.add(new PropertyInfo(itemId, ItemTemplateMgr.getInstance().getItemTempInfo(itemId).quality));
			rewardItemList.remove(index);
		}

		Collections.sort(randomItemIdList);
		for (PropertyInfo info : randomItemIdList) {
			netMsg.addRandomItemIdList(info.type);
		}

		long guildId = getGuildId();
		if (guildId > 0) {
			Guild guild = GuildMgr.getInstance().getGuildById(guildId);
			if (guild == null) {
				return;
			}

			guild.addGuildExp(1);
		}
	}

	public void pvpRandomItem() {
		PVPRandomDiceMsg.Builder netMsg = PVPRandomDiceMsg.newBuilder();
		int itemIndex = 0;
		for (int i = 0; i < 7; i++) {
			int color = ThreadLocalRandom.current().nextInt(1, 5);
			DiceInfoMsg.Builder diceInfoMsg = DiceInfoMsg.newBuilder();
			diceInfoMsg.setQuality(color);
			int[] aryColor = aryWhite;
			switch (color) {
			case 2:
				aryColor = aryBlack;
				break;

			case 3:
				aryColor = aryGold;
				break;

			case 4:
				aryColor = aryGreen;
				break;

			default:
				break;
			}

			int index = ThreadLocalRandom.current().nextInt(0, 6);
			itemIndex += aryColor[index];
			diceInfoMsg.setPointNum(index);
			netMsg.addDiceInfoList(diceInfoMsg);
		}
		int itemId = randomItemIdList.get(itemIndex).type;
		propMgr.addOnePropToPackage(itemId, 1, ItemChangeType.PVP_RANDOM_DICE_REWARD);
		netMsg.setItemIndex(itemIndex + 1);
		sendPacket(Protocol.S_C_PVP_RANDOM_ITEM, netMsg);
	}
}
