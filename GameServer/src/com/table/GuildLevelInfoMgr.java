package com.table;

import com.file.CTabFile;

public class GuildLevelInfoMgr {
	private GuildLevelInfo[] aryGuildLevelInfo;
	
	private static GuildLevelInfoMgr instance = new GuildLevelInfoMgr();

	public static GuildLevelInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryGuildLevelInfo = new GuildLevelInfo[len];
		for (int i = 0; i < len; ++i) {
			GuildLevelInfo info = new GuildLevelInfo();
			info.guildLv = file.getIntByColName(i, "level");
			info.needExp = file.getIntByColName(i, "exp");
			info.maxMemberCount = file.getIntByColName(i, "maxmember");
			info.dailyAward = file.getIntByColName(i, "dailyaward");
			aryGuildLevelInfo[i] = info;
		}
		return true;
	}
	
	public GuildLevelInfo getGuildLevelInfo(int guildLv) {
		for (GuildLevelInfo info : aryGuildLevelInfo) {
			if (info.guildLv == guildLv) {
				return info;
			}
		}
		return null;
	}
}
