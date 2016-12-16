package com.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.util.GameLog;

/*
 * 这个支持以Tab为分隔符的txt文件格式，支持的编码可以为UTF-8和Unicode，默认以UTF-8格式支持，因为考虑国际化。
 */

public class CTabFile
{
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

	public boolean load(String pathFileName)
	{

		this.pathFileName = pathFileName;
		List<String> contentList = new ArrayList<>();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(pathFileName + ".txt")))
		{
			contentList = br.lines().collect(Collectors.toList());
			colNames = contentList.get(0).split("\t");
			int colNum = colNames.length;
			totalRow = contentList.size() - 1;
			tableData = new String[totalRow][colNum];
			for (int i = 0; i < totalRow; i++)
			{
				tableData[i] = contentList.get(i + 1).split("\t", colNum);
			}
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private int getColIndex(String colName)
	{
		int colIndex = Integer.MAX_VALUE;
		for (int col = 0; col < colNames.length; ++col)
		{
			if (colNames[col].equals(colName))
			{
				colIndex = col;
				break;
			}
		}

		if (colIndex == Integer.MAX_VALUE)
		{
			GameLog.error("读取配置出错，文件名为: " + this.pathFileName + " 字段名为 " + colName + "不存在");
		}

		return colIndex;
	}

	public int getIntByColName(int row, String colName)
	{
		return Integer.parseInt(tableData[row][getColIndex(colName)]);
	}

	public long getLongByColName(int row, String colName)
	{
		return Long.parseLong(tableData[row][getColIndex(colName)]);
	}

	public String getStringByColName(int row, String colName)
	{
		return tableData[row][getColIndex(colName)];
	}

	public boolean getBoolByColName(int row, String colName)
	{
		return tableData[row][getColIndex(colName)].equals("1");
	}

	public float getFloatByColName(int row, String colName)
	{
		return Float.parseFloat(tableData[row][getColIndex(colName)]);
	}

	public double getDoubleByColName(int row, String colName)
	{
		return Double.parseDouble(tableData[row][getColIndex(colName)]);
	}

	public int getRows()
	{
		return totalRow;
	}

	public static void main(String[] str)
	{
		String fileName = "F:\\RushServer\\trunk\\Lib\\txt\\t_s_buffData";
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
		{
			System.exit(0);
		}

		int len = file.getRows();
		System.out.println("LZGLZG fileLen: " + len);
		for (int i = 0; i < len; ++i)
		{
			System.out.println("LZGLZG buffId: " + file.getIntByColName(i, "buffId"));
			System.out.println("LZGLZG shieldHpValue: " + file.getIntByColName(i, "shieldHpValue"));
		}
	}
}