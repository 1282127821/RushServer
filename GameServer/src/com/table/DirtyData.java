package com.table;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.util.GameLog;

public final class DirtyData {
	private List<String> dirtyArry = null;
	private static DirtyData instance = new DirtyData();
	public static DirtyData getInstance() {
		return instance;
	}
	
	public void initDirty(String path) {
		dirtyArry = new ArrayList<String>();
		try {
			readDirtyTxt(path, "UTF-8", dirtyArry, false);
		} catch (IOException e) {
			GameLog.error("加载脏字符异常 , patch : " + path, e);
		}
	}

	/**
	 * 根据给定的字符串和限制的字符串长度，如果合法则返回true，非法返回false
	 */
	public boolean isIllegal(String strName, int nameLenLimit) {
//		if (strName == null || strName.length() == 0 || strName.length() > nameLenLimit) {
//			return false;
//		}
//		
//		if (StringUtil.isContainsSpace(strName)) {
//			return false;
//		}
//
//		for (int i = 0; i < strName.length(); i++) {
//			char ch = strName.charAt(i);
//			if (ch == 0x27) {// 过滤 特殊符号 '
//				return false;
//			}
//			
//			if ((ch == 0x9) || (ch == 0xA) || (ch == 0xD) || ((ch >= 0x20) && (ch <= 0xD7FF)) || ((ch >= 0xE000) && (ch <= 0xFFFD)) || ((ch >= 0x10000) && (ch <= 0x10FFFF))) {
//			} else {
//				return false;
//			}
//		}
//		
//		for (int i = 0; i < dirtyArry.size(); i++) {
//			String dirtyStr = dirtyArry.get(i);
//			if (!dirtyStr.equals("") && strName.contains(dirtyStr)) {
//				return false;
//			}
//		}
		
		return true;
	}

	/**
	 * 读取脏字符文本文件内容
	 * 
	 * @param filePathAndName 带有完整绝对路径的文件名
	 * @param encoding 文本文件打开的编码方式
	 * @param dirtyArry 装脏字符的集合
	 * @param isProxy true:为代理商的脏字符文件，false:为内部脏字符文件
	 * @throws IOException
	 */
	private boolean readDirtyTxt(String filePathAndName, String encoding, List<String> dirtyArry, boolean isProxy) throws IOException {
		FileInputStream fs = null;
		BufferedReader br = null;
		try {
			encoding = encoding.trim();
			fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			br = new BufferedReader(isr);
			String data = "";
			int i = 0;
			while ((data = br.readLine()) != null) {
				if (!isProxy && i++ == 0) {
					dirtyArry.addAll(Arrays.asList(data.split("")));
					continue;
				}
				dirtyArry.addAll(Arrays.asList(data.split("\\|")));
			}
		} catch (Exception e) {
			GameLog.error("读取脏字符异常,filePath : " + filePathAndName + ",isProxy : " + isProxy, e);
			return false;
		} finally {
			if (br != null)
				br.close();
			if (fs != null)
				fs.close();
		}
		return true;
	}
}
