package com.star.light.table;

import com.star.light.file.CTabFile;
import com.star.light.prop.QualityType;

public class EquipBlessInfoMgr {
	private EquipBlessInfo[] aryBlessInfo;

	private static EquipBlessInfoMgr instance = new EquipBlessInfoMgr();

	public static EquipBlessInfoMgr getInstance() {
		return instance;
	}

	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryBlessInfo = new EquipBlessInfo[len];
		for (int i = 0; i < len; ++i) {
			EquipBlessInfo info = new EquipBlessInfo();
			info.blessLv1 = file.getIntByColName(i, "blesslevel1");
			info.blessLv2 = file.getIntByColName(i, "blesslevel2");
			info.blessLv3 = file.getIntByColName(i, "blesslevel3");
			info.blessLv4 = file.getIntByColName(i, "blesslevel4");
			info.material1Id = file.getIntByColName(i, "material1");
			info.material1Num = file.getIntByColName(i, "material1num");
			info.material2Id = file.getIntByColName(i, "material2");
			info.material2Num = file.getIntByColName(i, "material2num");
			aryBlessInfo[i] = info;
		}
		return true;
	}

	public EquipBlessInfo getEquipBlessInfo(int quality, int blessLv) {
		for (EquipBlessInfo info : aryBlessInfo) {
			int cfgBlessLv = 0;
			if (quality == QualityType.GREEN) {
				cfgBlessLv = info.blessLv1;
			} else if (quality == QualityType.BLUE) {
				cfgBlessLv = info.blessLv2;
			} else if (quality == QualityType.PURPLE) {
				cfgBlessLv = info.blessLv3;
			} else if (quality == QualityType.ORANGE) {
				cfgBlessLv = info.blessLv4;
			}
			
			if (blessLv <= cfgBlessLv) {
				return info;
			}
		}
		return null;
	}
}
