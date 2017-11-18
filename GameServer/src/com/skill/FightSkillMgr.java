package com.skill;

import java.util.ArrayList;
import java.util.List;

import com.BaseServer;
import com.db.DBOption;
import com.pbmessage.GamePBMsg.SkillChainMsg;
import com.pbmessage.GamePBMsg.SkillInfoMsg;
import com.pbmessage.GamePBMsg.SkillMsg;
import com.player.DaoMgr;
import com.player.GamePlayer;
import com.player.ItemChangeType;
import com.protocol.Protocol;
import com.table.ConfigMgr;
import com.table.SkillTemplateMgr;
import com.util.Log;

public class FightSkillMgr {
	private GamePlayer player;

	/**
	 * 玩家所拥有的技能
	 */
	private List<FightSkillUnit> playerSkillList;

	/**
	 * 玩家技能链信息
	 */
	private SkillChainInfo skillChainInfo;

	public FightSkillMgr(GamePlayer player) {
		this.player = player;
		playerSkillList = new ArrayList<FightSkillUnit>();
	}

	/**
	 * 从数据库中加载技能信息
	 */
	public void loadFromDB() {
		int jobType = player.getJobId();
		long userId = player.getUserId();
		DaoMgr.fightSkillInfoDao.getFightSkillInfo(userId, jobType, playerSkillList);
		checkSkillUnlock(player.getPlayerLv(), jobType);
		skillChainInfo = DaoMgr.fightSkillInfoDao.getSkillChain(userId, jobType);
		if (skillChainInfo == null) {
			skillChainInfo = new SkillChainInfo();
			int[] arySkillChain = new int[] { -2, -2, -2, -2, -2, -2 };
			int skillLen = arySkillChain.length;
			for (int i = 0, len = arySkillChain.length; i < len; i++) {
				if (i < skillLen) {
					int index = 0;
					for (FightSkillUnit info : playerSkillList) {
						if (info.isActiveSkill() && index < skillLen) {
							arySkillChain[index++] = info.getSkillId();
						}
					}

					index = 4;
					for (FightSkillUnit info : playerSkillList) {
						if (!info.isActiveSkill() && index < skillLen) {
							arySkillChain[index++] = info.getSkillId();
						}
					}
				}
			}
			skillChainInfo.setArySkillChain(arySkillChain);
			skillChainInfo.setOp(DBOption.INSERT);
		}
		syncSkillInfo();
	}

	/**
	 * 根据等级检测技能是否可以解锁并把技能添加到角色身上
	 */
	public void checkSkillUnlock(int level, int jobType) {
		FightSkillInfo[] arySkillInfo = SkillTemplateMgr.getInstance().getUnlockFightSkillInfo(jobType);
		for (int i = 0; i < arySkillInfo.length; i++) {
			FightSkillInfo info = arySkillInfo[i];
			if (info != null) {
				int skillId = info.skillId;
				boolean isUnlock = level >= info.unlockLv && getSkillUnitBySkillId(skillId) == null;
				if (isUnlock) {
					FightSkillUnit newSkillInfo = new FightSkillUnit();
					newSkillInfo.setSkillDBId(BaseServer.IDWORK.nextId());
					newSkillInfo.setSkillId(skillId);
					newSkillInfo.setSkillLv(1);
					newSkillInfo.setActiveSkill(true);
					newSkillInfo.setOp(DBOption.INSERT);
					playerSkillList.add(newSkillInfo);
				}
			}
		}

		List<PassiveSkillInfo> skillInfoList = SkillTemplateMgr.getInstance().getPlayerPassiveSkill(jobType);
		for (PassiveSkillInfo info : skillInfoList) {
			int skillId = info.skillId;
			if (getSkillUnitBySkillId(skillId) == null) {
				FightSkillUnit newSkillInfo = new FightSkillUnit();
				newSkillInfo.setSkillDBId(BaseServer.IDWORK.nextId());
				newSkillInfo.setSkillId(skillId);
				newSkillInfo.setSkillLv(1);
				newSkillInfo.setActiveSkill(false);
				newSkillInfo.setOp(DBOption.INSERT);
				playerSkillList.add(newSkillInfo);
			}
		}
	}

	/**
	 * 玩家升级之后解锁技能和同步技能信息给客户端
	 */
	public void playerLvUpUnlockSkill(int level, int jobType) {
		checkSkillUnlock(level, jobType);
		syncSkillInfo();
	}

	/**
	 * 同步技能信息
	 */
	public void syncSkillInfo() {
		SkillMsg.Builder netMsg = SkillMsg.newBuilder();
		buildWrite(netMsg);
		player.sendPacket(Protocol.S_C_SKILL_INFO, netMsg);
	}

	public void buildWrite(SkillMsg.Builder netMsg) {
		for (FightSkillUnit info : playerSkillList) {
			SkillInfoMsg.Builder infoMsg = SkillInfoMsg.newBuilder();
			infoMsg.setSkillId(info.getSkillId());
			infoMsg.setSkillLv(info.getSkillLv());
			infoMsg.setIsActiveSkill(info.isActiveSkill());
			netMsg.addSkillInfoList(infoMsg);
		}

		int[] arySkillChain = skillChainInfo.getArySkillChain();
		for (int i = 0; i < arySkillChain.length; i++) {
			netMsg.addSkillChainList(arySkillChain[i]);
		}
	}

	/**
	 * 玩家升级技能，根据operType来确定是单独升级还是一键升级
	 */
	public void skillLevelUp(int skillId, int upgradeType) {
		FightSkillUnit skillInfo = getSkillUnitBySkillId(skillId);
		if (skillInfo == null) {
			Log.error("skillLevelUp 技能Id为: " + skillId + " 并未学习，" + "UserId: " + player.getUserId());
			return;
		}

		int maxSkillLv = ConfigMgr.maxSkillLv;
		if (!skillInfo.isActiveSkill()) {
			maxSkillLv = 5;
		}
		int curSkillLv = skillInfo.getSkillLv();
		if (curSkillLv == maxSkillLv) {
			Log.error("skillLevelUp 当前技能等级已达最高级，等级为:   " + curSkillLv + "，技能Id为:  " + skillId + "，UserId: "
					+ player.getUserId());
			return;
		}
		
		int costGold = 1000;
		if (skillInfo.isActiveSkill()) {
			costGold = SkillTemplateMgr.getInstance().getFightSkillInfo(player.getJobId(), skillId).upgradeGold;
		} else {
			costGold = SkillTemplateMgr.getInstance().getPassiveSkillInfo(skillId + curSkillLv - 1).upgradeGold;
		}

		while (player.removeGold(costGold, ItemChangeType.UPGRADE_SKILL)) { // 当技能等级升级限制小于等于玩家等级时
			curSkillLv += 1;
			if (upgradeType == 1 || curSkillLv == maxSkillLv) {
				break;
			}
		}

		skillInfo.setSkillLv(curSkillLv);
		SkillInfoMsg.Builder netMsg = SkillInfoMsg.newBuilder();
		netMsg.setSkillId(skillId);
		netMsg.setSkillLv(curSkillLv);
		player.sendPacket(Protocol.S_C_UPGRADE_SKILL, netMsg);
	}

	/**
	 * 根据技能Id获得技能信息
	 */
	private FightSkillUnit getSkillUnitBySkillId(int skillId) {
		for (FightSkillUnit skillInfo : playerSkillList) {
			if (skillInfo.getSkillId() == skillId) {
				return skillInfo;
			}
		}

		return null;
	}

	public void setFightSkillChain(SkillChainMsg msg) {
		int skillId = msg.getSkillId();
		FightSkillUnit skillUnit = getSkillUnitBySkillId(skillId);
		if (skillUnit == null) {
			return;
		}
		
		int[] arySkillChain = skillChainInfo.getArySkillChain();
		boolean isExchange = false;
		int skillPos = msg.getSkillSrcPos();
		for (int i = 0, len = arySkillChain.length; i < len; i++) {
			if (skillId == arySkillChain[i]) {
				isExchange = true;
				exchangeSkillChain(skillPos, i + 1);
				break;
			}
		}
		
		if (!isExchange) {
			if (skillUnit.isActiveSkill() && skillPos >= 1 && skillPos <= 4) {
				arySkillChain[skillPos - 1] = skillId;
			} else if (!skillUnit.isActiveSkill() && skillPos >= 5 && skillPos <= 6) {
				arySkillChain[skillPos - 1] = skillId;
			}
		}

		skillChainInfo.setArySkillChain(arySkillChain);
		SkillMsg.Builder netMsg = SkillMsg.newBuilder();
		for (int i = 0; i < arySkillChain.length; i++) {
			netMsg.addSkillChainList(arySkillChain[i]);
		}
		player.sendPacket(Protocol.S_C_FIGHT_SKILL_CHAIN, netMsg);
	}

	public void exchangeSkillChain(int srcPos, int destPos) {
		int[] arySkillChain = skillChainInfo.getArySkillChain();
		int skillChainLen = arySkillChain.length;
		if (srcPos < 1 || srcPos > skillChainLen || destPos < 1 || destPos > skillChainLen) {
			return;
		}

		if ((srcPos == 5 || srcPos == 6) && (destPos == 6 || destPos == 5)) {
			int srcSkillId = arySkillChain[srcPos - 1];
			arySkillChain[srcPos - 1] = arySkillChain[destPos - 1];
			arySkillChain[destPos - 1] = srcSkillId;
		} else if (srcPos >= 1 && destPos >= 1) {
			int srcSkillId = arySkillChain[srcPos - 1];
			arySkillChain[srcPos - 1] = arySkillChain[destPos - 1];
			arySkillChain[destPos - 1] = srcSkillId;
		}

		skillChainInfo.setArySkillChain(arySkillChain);
		SkillMsg.Builder netMsg = SkillMsg.newBuilder();
		for (int i = 0; i < arySkillChain.length; i++) {
			netMsg.addSkillChainList(arySkillChain[i]);
		}
		player.sendPacket(Protocol.S_C_FIGHT_SKILL_CHAIN, netMsg);
	}

	/**
	 * 从内存中卸载玩家的技能数据
	 */
	public void unloadData() {
		playerSkillList = null;
		skillChainInfo = null;
	}

	/**
	 * 将玩家的技能信息保存到数据库中
	 */
	public void saveToDB(int jobType) {
		long userId = player.getUserId();
		try {
			List<FightSkillUnit> saveList = new ArrayList<FightSkillUnit>(playerSkillList);
			for (FightSkillUnit info : saveList) {
				if (info.getOp() == DBOption.INSERT) {
					DaoMgr.fightSkillInfoDao.addFightSkillInfo(userId, jobType, info);
				}

				if (info.getOp() == DBOption.UPDATE) {
					DaoMgr.fightSkillInfoDao.updateFightSkillInfo(userId, info);
				}

				if (skillChainInfo.getOp() == DBOption.INSERT) {
					DaoMgr.fightSkillInfoDao.insertSkillChain(userId, jobType, skillChainInfo);
				} else if (skillChainInfo.getOp() == DBOption.UPDATE) {
					DaoMgr.fightSkillInfoDao.updateSkillChain(userId, jobType, skillChainInfo);
				}
			}
		} catch (Exception e) {
			Log.error("保存玩家技能信息出错, UserId:  " + userId, e);
		}
	}
}