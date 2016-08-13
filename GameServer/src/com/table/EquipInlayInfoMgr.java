package com.table;

import com.file.CTabFile;
import com.util.SplitUtil;

public class EquipInlayInfoMgr {
	private EquipInlayInfo[] aryInlayCardInfo;
	
	private static EquipInlayInfoMgr instance = new EquipInlayInfoMgr();

	public static EquipInlayInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryInlayCardInfo = new EquipInlayInfo[len];
		for (int i = 0; i < len; ++i) {
			EquipInlayInfo info = new EquipInlayInfo();
			info.cardId = file.getIntByColName(i, "id");
			info.inlayPos = SplitUtil.splitToInt(file.getStringByColName(i, "part"));
			aryInlayCardInfo[i] = info;
		}
		return true;
	}
	
	public EquipInlayInfo getInlayCardInfo(int cardId) {
		for (EquipInlayInfo info : aryInlayCardInfo) {
			if (info.cardId == cardId) {
				return info;
			}
		}
		return null;
	}
}
