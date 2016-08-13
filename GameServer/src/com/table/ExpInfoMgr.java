package com.table;

import com.file.CTabFile;

public final class ExpInfoMgr {
	private ExpInfo[] aryExpInfo;
	
	private static ExpInfoMgr instance = new ExpInfoMgr();

	public static ExpInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryExpInfo = new ExpInfo[len];
		for (int i = 0; i < len; ++i) {
			ExpInfo info = new ExpInfo();
			info.level = file.getIntByColName(i, "level");
			info.needExp = file.getIntByColName(i, "needexp");
			info.totalExp = file.getIntByColName(i, "sumexp");
			aryExpInfo[i] = info;
		}
		return true;
	}
	
	public ExpInfo getExpInfo(int level) {
		for (ExpInfo info : aryExpInfo) {
			if (info.level == level) {
				return info;
			}
		}
		return null;
	}
}
