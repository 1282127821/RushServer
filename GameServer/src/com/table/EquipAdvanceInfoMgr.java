package com.table;

import com.file.CTabFile;

public class EquipAdvanceInfoMgr {
	private EquipAdvanceInfo[] aryEquipAdvanceInfo;
	
	private static EquipAdvanceInfoMgr instance = new EquipAdvanceInfoMgr();

	public static EquipAdvanceInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryEquipAdvanceInfo = new EquipAdvanceInfo[len];
		for (int i = 0; i < len; ++i) {
			EquipAdvanceInfo info = new EquipAdvanceInfo();
			info.equipQuality = file.getIntByColName(i, "rarelevel");
			info.lowMaterialId = file.getIntByColName(i, "materialid");
			info.lowMaterialChance = file.getIntByColName(i, "normalmaterial");
			info.highMaterialId = file.getIntByColName(i, "highmaterialid");
			info.highMaterialChance = file.getIntByColName(i, "highmaterial");
			aryEquipAdvanceInfo[i] = info;
		}
		return true;
	}
	
	public EquipAdvanceInfo getEquipAdvanceInfo(int equipQuality) {
		for (EquipAdvanceInfo info : aryEquipAdvanceInfo) {
			if (info.equipQuality == equipQuality) {
				return info;
			}
		}
		return null;
	}
}
