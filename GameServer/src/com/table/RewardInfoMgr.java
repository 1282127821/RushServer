package com.table;

import com.file.CTabFile;
import com.util.SplitUtil;

public final class RewardInfoMgr {
	private DropRewardInfo[] dropRewards;
	private static RewardInfoMgr instance = new RewardInfoMgr();

	public static RewardInfoMgr getInstance() {
		return instance;
	}

	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		int len = file.getRows();
		dropRewards = new DropRewardInfo[len];
		for (int i = 0; i < len; ++i) {
			DropRewardInfo info = new DropRewardInfo();
			info.dropId = file.getIntByColName(i, "dropid");
			info.aryDropItem = SplitUtil.splitToInt(file.getStringByColName(i, "goodsid"));
			info.aryDropItemNum = SplitUtil.splitToInt(file.getStringByColName(i, "num"));
			info.aryItemDropChance = SplitUtil.splitToInt(file.getStringByColName(i, "addProbability"));
			info.aryItemNumLimit = SplitUtil.splitToInt(file.getStringByColName(i, "numberlimit"));
			dropRewards[i] = info;
		}

		return true;
	}

	public DropRewardInfo getDropRewardInfo(int dropId) {
		for (DropRewardInfo info : dropRewards) {
			if (info.dropId == dropId) {
				return info;
			}
		}

		return null;
	}
}
