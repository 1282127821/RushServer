package com.table;

import com.util.CTabFile;

public class EquipBlessEffectInfoMgr {
	private EquipBlessEffectInfo[] aryEquipBlessEffectInfo;
	
	private static EquipBlessEffectInfoMgr instance = new EquipBlessEffectInfoMgr();

	public static EquipBlessEffectInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryEquipBlessEffectInfo = new EquipBlessEffectInfo[len];
		for (int i = 0; i < len; ++i) {
			EquipBlessEffectInfo info = new EquipBlessEffectInfo();
			info.equipQuality = file.getIntByColName(i, "rarelevel");
			info.safeLv = file.getIntByColName(i, "safelevel");
			info.fixedAttack = file.getIntByColName(i, "fixatk");
			info.minDangerAttack = file.getIntByColName(i, "dangeratkmin");
			info.maxDangerAttack = file.getIntByColName(i, "dangeratkmax");
			info.addAttack = file.getIntByColName(i, "addatk");
			info.fixedDefence = file.getIntByColName(i, "fixdef");
			info.minDangerDefence = file.getIntByColName(i, "dangerdefmin");
			info.maxDangerDefence = file.getIntByColName(i, "dangerdefmax");
			info.addDefence = file.getIntByColName(i, "adddef");
			aryEquipBlessEffectInfo[i] = info;
		}
		return true;
	}
	
	public EquipBlessEffectInfo getEquipBlessEffectInfo(int equipQuality) {
		for (EquipBlessEffectInfo info : aryEquipBlessEffectInfo) {
			if (info.equipQuality == equipQuality) {
				return info;
			}
		}
		return null;
	}
}
