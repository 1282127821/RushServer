package com.star.light.table;

import com.star.light.file.CTabFile;

public class CharacterInfoMgr {
	private CharacterInfo[] aryCharacterInfo;
	
	private static CharacterInfoMgr instance = new CharacterInfoMgr();

	public static CharacterInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryCharacterInfo = new CharacterInfo[len];
		for (int i = 0; i < len; ++i) {
			CharacterInfo info = new CharacterInfo();
			info.charId = file.getIntByColName(i, "CharId");
			info.charHP = file.getIntByColName(i, "Live");
			aryCharacterInfo[i] = info;
		}
		return true;
	}
	
	public CharacterInfo getCharacterInfo(int charId) {
		for (CharacterInfo info : aryCharacterInfo) {
			if (info.charId == charId) {
				return info;
			}
		}
		return null;
	}
}
