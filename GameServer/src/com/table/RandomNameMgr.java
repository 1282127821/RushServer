package com.table;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.util.CTabFile;

public class RandomNameMgr {
	private RandomNameInfo[] aryUserName;
	private static RandomNameMgr instance = new RandomNameMgr();
	public static RandomNameMgr getInstance() {
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		int len = file.getRows();
		aryUserName = new RandomNameInfo[len];
		for (int i = 0; i < len; ++i) {
			RandomNameInfo info = new RandomNameInfo();
			info.setType(file.getIntByColName(i, "Category"));
			info.setValue(file.getStringByColName(i, "Content"));
			aryUserName[i] = info;
		}

		return true;
	}
	
	public String randomRoleName(boolean isMale) {
		List<String> firstName = new ArrayList<String>();
		List<String> lastName = new ArrayList<String>();
		for (RandomNameInfo nameInfo : aryUserName) {
			int nameType = nameInfo.getType();
			String nameValue = nameInfo.getValue();
			if(nameType == 1){
				firstName.add(nameValue);
			} else if(nameType == 2 && isMale){
				lastName.add(nameValue);
			} else if(nameType == 3 && !isMale){
				lastName.add(nameValue);
			}
		}
		
		int firstindex = ThreadLocalRandom.current().nextInt(0, firstName.size());
		int lastindex = ThreadLocalRandom.current().nextInt(0, lastName.size());
		return firstName.get(firstindex) + lastName.get(lastindex);
	}
}
