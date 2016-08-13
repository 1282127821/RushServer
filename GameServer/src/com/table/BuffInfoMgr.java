package com.table;

import com.file.CTabFile;

public class BuffInfoMgr {
	private BuffInfo[] aryBuffInfo;

	private static BuffInfoMgr instance = new BuffInfoMgr();

	public static BuffInfoMgr getInstance() {
		return instance;
	}

	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryBuffInfo = new BuffInfo[len];
		for (int i = 0; i < len; ++i) {
			BuffInfo info = new BuffInfo();
			info.buffId = file.getIntByColName(i, "buffId");
			info.shieldHP = file.getIntByColName(i, "shieldHpValue");
			aryBuffInfo[i] = info;
		}
		return true;
	}

	public BuffInfo getBuffInfo(int buffId) {
		for (BuffInfo info : aryBuffInfo) {
			if (info.buffId == buffId) {
				return info;
			}
		}
		return null;
	}
}
