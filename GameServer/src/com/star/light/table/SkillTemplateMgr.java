package com.star.light.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.star.light.file.CTabFile;
import com.star.light.player.GameConst;
import com.star.light.player.JobType;
import com.star.light.skill.FightSkillInfo;
import com.star.light.skill.PassiveSkillInfo;
import com.star.light.skill.SkillType;

public final class SkillTemplateMgr {
	/**
	 * 玩家的解锁技能
	 */
	private Map<Integer, FightSkillInfo[]> playerFightSkillInfoMap;
	
	/**
	 * 玩家的被动技能集合
	 */
	private Map<Integer, List<PassiveSkillInfo>> playerPassiveSkill;
	
	/**
	 * 玩家的被动技能
	 */
	private PassiveSkillInfo[] aryPassiveSkill;

	private static SkillTemplateMgr instance = new SkillTemplateMgr();

	public static SkillTemplateMgr getInstance() {
		return instance;
	}

	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		playerFightSkillInfoMap = new HashMap<Integer, FightSkillInfo[]>();
		int skillIndex = 0;
		for (int i = 0; i < file.getRows(); ++i) {
			int jobType = file.getIntByColName(i, "Job");
			if (jobType >= JobType.MAN && jobType <= JobType.LOLI) {
				FightSkillInfo skillInfo = new FightSkillInfo();
				int skillId = file.getIntByColName(i, "SkillID");
				skillInfo.skillId = skillId;
				skillInfo.jobType = jobType;
				skillInfo.skillCD = file.getIntByColName(i, "skillCD");
				int skillType = file.getIntByColName(i, "SkillType");
				skillInfo.unlockLv = file.getIntByColName(i, "UnlockLevel");
				skillInfo.upgradeGold = file.getIntByColName(i, "UpgradeCost");
				if ((skillType == SkillType.OTHER || skillType == SkillType.NORMAL
						|| skillType == SkillType.LEVELUP_ZERO) && skillInfo.skillCD > 0) {
					FightSkillInfo[] playerSkill = playerFightSkillInfoMap.get(jobType);
					if (playerSkill == null) {
						skillIndex = 0;
						playerSkill = new FightSkillInfo[GameConst.PER_JOB_SKILL_COUNT];
						playerFightSkillInfoMap.put(jobType, playerSkill);
					}
					playerSkill[skillIndex++] = skillInfo;
				}
			}
		}

		return true;
	}
	
	public boolean loadPassiveSkill(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		playerPassiveSkill = new HashMap<Integer, List<PassiveSkillInfo>>();
		aryPassiveSkill = new PassiveSkillInfo[file.getRows()];
		for (int i = 0; i < file.getRows(); ++i) {
			PassiveSkillInfo skillInfo = new PassiveSkillInfo();
			skillInfo.skillId = file.getIntByColName(i, "SkillID");
			int jobType = file.getIntByColName(i, "JobType");
			skillInfo.jobType = jobType;
			skillInfo.effectType = file.getIntByColName(i, "effecttype");
			skillInfo.effectValue = file.getIntByColName(i, "effectvalue");
			skillInfo.skillLv = file.getIntByColName(i, "Skilllevel");
			skillInfo.skillLearnLv = file.getIntByColName(i, "SkillLearnLevel");
			skillInfo.upgradeGold = file.getIntByColName(i, "GoldUpgrade");
			List<PassiveSkillInfo> playerSkill = playerPassiveSkill.get(jobType);
			if (playerSkill == null) {
				playerSkill = new ArrayList<PassiveSkillInfo>();
				playerPassiveSkill.put(jobType, playerSkill);
			}
			if (skillInfo.skillLv == 1) {
				playerSkill.add(skillInfo);
			}
			aryPassiveSkill[i] = skillInfo;
		}

		return true;
	}
	
	public FightSkillInfo[] getUnlockFightSkillInfo(int jobType) {
		return playerFightSkillInfoMap.get(jobType);
	}

	public FightSkillInfo getFightSkillInfo(int jobType, int skillId) {
		FightSkillInfo[] arySkillInfo = SkillTemplateMgr.getInstance().getUnlockFightSkillInfo(jobType);
		for (int i = 0; i < arySkillInfo.length; i++) {
			FightSkillInfo info = arySkillInfo[i];
			if (info.skillId == skillId) {
				return info;
			}
		}

		return null;
	}
	
	public List<PassiveSkillInfo> getPlayerPassiveSkill(int jobType) {
		return playerPassiveSkill.get(jobType);
	}
	
	public PassiveSkillInfo getPassiveSkillInfo(int skillId) {
		for (PassiveSkillInfo info : aryPassiveSkill) {
			if (info.skillId == skillId) {
				return info;
			}
		}
		
		return null;
	}
}