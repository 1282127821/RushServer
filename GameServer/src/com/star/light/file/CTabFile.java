package com.star.light.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import com.star.light.util.GameLog;
import com.star.light.util.StringUtil;

/*
 * 这个支持以Tab为分隔符的txt文件格式，支持的编码可以为UTF-8和Unicode，默认以UTF-8格式支持，因为考虑国际化。
 */

public class CTabFile {
	/**
	 * 以字符串的格式存放数据
	 */
	private String[][] tableData;

	/**
	 * 字段的名字
	 */
	private String[] colNames;

	/**
	 * 配置文件名
	 */
	private String pathFileName;

	/**
	 * 配置的总行数
	 */
	private int totalRow;

	public boolean load(String pathFileName) {
		try {
			this.pathFileName = pathFileName;
			FileInputStream fs = new FileInputStream(pathFileName + ".txt");
			InputStreamReader isr = new InputStreamReader(fs, "UTF-8");
			BufferedReader bufReader = new BufferedReader(isr);
			String strEnglishName = bufReader.readLine();
			if (StringUtil.isNullOrEmpty(strEnglishName)) {
				bufReader.close();
				return false;
			}

			colNames = strEnglishName.split("\t");
			int colNum = colNames.length;
			List<String> contentList = bufReader.lines().collect(Collectors.toList());
			totalRow = contentList.size();
			tableData = new String[totalRow][colNum];
			for (int i = 0; i < totalRow; i++) {
				tableData[i] = contentList.get(i).split("\t", colNum);
			}

			bufReader.close();
			isr.close();
			fs.close();
			return true;
		} catch (FileNotFoundException e) {
			GameLog.error("配置文件不存在", e);
			return false;
		} catch (IOException e) {
			GameLog.error("打开文件失败", e);
			return false;
		}
	}

	private int getColIndex(String colName) {
		int colIndex = Integer.MAX_VALUE;
		for (int col = 0; col < colNames.length; ++col) {
			if (colNames[col].equals(colName)) {
				colIndex = col;
				break;
			}
		}

		if (colIndex == Integer.MAX_VALUE) {
			GameLog.error("读取配置出错，文件名为: " + this.pathFileName + " 字段名为 " + colName + "不存在");
		}

		return colIndex;
	}

	public int getIntByColName(int row, String colName) {
		return getInt(row, getColIndex(colName));
	}

	public int getInt(int row, int col) {
		return Integer.parseInt(tableData[row][col]);
	}

	public long getLongByColName(int row, String colName) {
		return getLong(row, getColIndex(colName));
	}

	public long getLong(int row, int col) {
		return Long.parseLong(tableData[row][col]);
	}

	public String getStringByColName(int row, String colName) {
		return getString(row, getColIndex(colName));
	}

	public String getString(int row, int col) {
		return tableData[row][col];
	}

	public boolean getBoolByColName(int row, String colName) {
		return getBool(row, getColIndex(colName));
	}

	public boolean getBool(int row, int col) {
		return getBool(row, col, false);
	}

	public boolean getBool(int row, int col, Boolean defaultValue) {
		return tableData[row][col].equals("1");
	}

	public float getFloatByColName(int row, String colName) {
		return getFloat(row, getColIndex(colName));
	}

	public float getFloat(int row, int col) {
		return Float.parseFloat(tableData[row][col]);
	}

	public double getDoubleByColName(int row, String colName) {
		return getDouble(row, getColIndex(colName));
	}

	public double getDouble(int row, int col) {
		return Double.parseDouble(tableData[row][col]);
	}

	public int getRows() {
		return totalRow;
	}
}