package com.star.light.table;

import com.star.light.file.CTabFile;
import com.star.light.util.SplitUtil;

public final class LevelStageInfoMgr {
	private LevelStageInfo[] aryStageInfo;
	
	private static LevelStageInfoMgr instance = new LevelStageInfoMgr();

	public static LevelStageInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		int len = file.getRows();
		aryStageInfo = new LevelStageInfo[len];
		for (int i = 0; i < len; i++) {
			LevelStageInfo info = new LevelStageInfo();
			info.stageId = file.getIntByColName(i, "stageId");
			info.stageType = file.getIntByColName(i, "stagetype");
			info.rewardExp = file.getIntByColName(i, "exp");
			info.rewardMoney = file.getIntByColName(i, "money");
			info.rewardDropId = file.getIntByColName(i, "dropid");
			info.monsterId = SplitUtil.splitToInt(file.getStringByColName(i, "stageMonsterCharId"));
			info.monsterNodeId = SplitUtil.splitToInt(file.getStringByColName(i, "stageMonsterNodeId"));
			aryStageInfo[i] = info;
		}
		return true;
	}

	public LevelStageInfo getLevelStageInfo(int stageId) {
		for (LevelStageInfo info : aryStageInfo) {
			if (info.stageId == stageId) {
				return info;
			}
		}
		return null;
	}
}