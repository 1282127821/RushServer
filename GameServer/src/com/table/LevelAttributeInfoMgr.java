package com.table;

import com.player.FightAttributeType;
import com.util.CTabFile;

public class LevelAttributeInfoMgr {
	private LevelAttributeInfo[] aryLevelAttributeInfo;
	
	private static LevelAttributeInfoMgr instance = new LevelAttributeInfoMgr();

	public static LevelAttributeInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryLevelAttributeInfo = new LevelAttributeInfo[len];
		for (int i = 0; i < len; ++i) {
			LevelAttributeInfo info = new LevelAttributeInfo();
			info.jodId = file.getIntByColName(i, "jobid");
			info.level = file.getIntByColName(i, "level");
			
			int[] aryAttributeValue = new int[FightAttributeType.COUNT];
			aryAttributeValue[FightAttributeType.HP] = file.getIntByColName(i, "hp");
			aryAttributeValue[FightAttributeType.MP] = file.getIntByColName(i, "mp");
			aryAttributeValue[FightAttributeType.STRENGTH] = file.getIntByColName(i, "str");
			aryAttributeValue[FightAttributeType.AGILITY] = file.getIntByColName(i, "agi");
			aryAttributeValue[FightAttributeType.VITALITY] = file.getIntByColName(i, "vit");
			aryAttributeValue[FightAttributeType.INTELLIGENCE] = file.getIntByColName(i, "int");
			aryAttributeValue[FightAttributeType.DEXTERITY] = file.getIntByColName(i, "dex");
			aryAttributeValue[FightAttributeType.LUCKY] = file.getIntByColName(i, "luk");
			aryAttributeValue[FightAttributeType.ATTACK] = file.getIntByColName(i, "atk");
			aryAttributeValue[FightAttributeType.DEFENCE] = file.getIntByColName(i, "def");
			aryAttributeValue[FightAttributeType.MAGIC_ATTACK] = file.getIntByColName(i, "matk");
			aryAttributeValue[FightAttributeType.MAGIC_DEFENCE] = file.getIntByColName(i, "mdef");
			aryAttributeValue[FightAttributeType.ATTACK_SPEED] = (int)Math.ceil(file.getFloatByColName(i, "aspeed"));
			aryAttributeValue[FightAttributeType.MAGIC_COLD] = (int)Math.ceil(file.getFloatByColName(i, "mcold"));
			aryAttributeValue[FightAttributeType.HIT] = file.getIntByColName(i, "hit");
			aryAttributeValue[FightAttributeType.DODGE] = file.getIntByColName(i, "dog");
			info.attributeValue = aryAttributeValue;
			aryLevelAttributeInfo[i] = info;
		}
		return true;
	}
	
	public LevelAttributeInfo getLevelAttributeInfo(int jobId, int level) {
		for (LevelAttributeInfo info : aryLevelAttributeInfo) {
			if (info.jodId == jobId && info.level == level) {
				return info;
			}
		}
		return null;
	}
}
