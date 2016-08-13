package com.table;

import com.file.CTabFile;

public final class MainTaskInfoMgr {
	private MainTaskInfo[] aryMainTask;
	
	private static MainTaskInfoMgr instance = new MainTaskInfoMgr();

	public static MainTaskInfoMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		int len = file.getRows();
		aryMainTask = new MainTaskInfo[len];
		for (int i = 0; i < len; i++) {
			MainTaskInfo info = new MainTaskInfo();
			info.taskId = file.getIntByColName(i, "ID");
			info.stageId = file.getIntByColName(i, "stageId");
			aryMainTask[i] = info;
		}
		return true;
	}

	public MainTaskInfo getMainTaskInfo(int taskId) {
		for (MainTaskInfo info : aryMainTask) {
			if (info.taskId == taskId) {
				return info;
			}
		}
		return null;
	}
}
