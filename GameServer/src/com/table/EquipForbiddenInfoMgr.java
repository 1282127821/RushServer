package com.table;

import com.file.CTabFile;

public class EquipForbiddenInfoMgr {
	private EquipForbiddenInfo equipForbiddenInfo;
	
	private static EquipForbiddenInfoMgr instance = new EquipForbiddenInfoMgr();

	public static EquipForbiddenInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		equipForbiddenInfo = new EquipForbiddenInfo();
		int[] aryAdvanceForbidden = new int[len];
		int[] aryBlessForbidden = new int[len];
		int[] aryInlayForbidden = new int[len];
		for (int i = 0; i < len; ++i) {
			aryAdvanceForbidden[i] = file.getIntByColName(i, "setforbidden");
			aryBlessForbidden[i] = file.getIntByColName(i, "blessforbidden");
			aryInlayForbidden[i] = file.getIntByColName(i, "advanceforbidden");
		}
		
		equipForbiddenInfo.aryAdvanceForbidden = aryAdvanceForbidden;
		equipForbiddenInfo.aryBlessForbidden = aryBlessForbidden;
		equipForbiddenInfo.aryInlayForbidden = aryInlayForbidden;
		return true;
	}
	
	public boolean isEquipAdvanceForbidden(int equipId) {
		int[] aryAdvanceForbidden = equipForbiddenInfo.aryAdvanceForbidden;
		for (int i = 0, len = aryAdvanceForbidden.length; i < len; ++i) {
			if (aryAdvanceForbidden[i] == equipId) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEquipBlessForbidden(int equipId) {
		int[] aryAdvanceForbidden = equipForbiddenInfo.aryBlessForbidden;
		for (int i = 0, len = aryAdvanceForbidden.length; i < len; ++i) {
			if (aryAdvanceForbidden[i] == equipId) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEquipInlayForbidden(int equipId) {
		int[] aryAdvanceForbidden = equipForbiddenInfo.aryInlayForbidden;
		for (int i = 0, len = aryAdvanceForbidden.length; i < len; ++i) {
			if (aryAdvanceForbidden[i] == equipId) {
				return true;
			}
		}
		
		return false;
	}
}
