package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SplitUtil {
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String SEMICOLON = ";";
	public static final String DELIMITER_ARGS = ":";
	public static final String BETWEEN_ITEMS = ",";
	public static final String ELEMENT_SPLIT = "\\|";
	public static final String ELEMENT_DELIMITER = "|";
	public static final String ATTRIBUTE_SPLIT = "_";
	public static final String LEFT_PARENTH_SPLIT = "[";
	public static final String RIGHT_PARENTH_SPLIT = "]";
	public static final String LEFT_ELEMENT_SPLIT = "_[";
	public static final String ATTRIBUTE_SPLITE_1 = "#";
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	public static final long[] EMPTY_LONG_ARRAY = new long[0];
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	public static int[] splitToInt(String str) {
		return splitToInt(str, ",");
	}

	public static int[] splitToInt(String str, String spStr) {
		if (str.trim().length() == 0) {
			return EMPTY_INT_ARRAY;
		}

		String[] temps = str.split(spStr);
		int len = temps.length;
		int[] results = new int[len];
		for (int i = 0; i < len; i++) {
			results[i] = Integer.parseInt(temps[i].trim());
		}
		return results;
	}

	public static long[] splitToLong(String str) {
		return splitToLong(str, ",");
	}

	public static long[] splitToLong(String str, String spStr) {
		if (str.trim().length() == 0) {
			return EMPTY_LONG_ARRAY;
		}

		String[] temps = str.split(spStr);
		int len = temps.length;
		long[] results = new long[len];
		for (int i = 0; i < len; i++) {
			results[i] = Long.parseLong(temps[i].trim());
		}
		return results;
	}

	public static double[] splitToDouble(String str) {
		return splitToDouble(str, ",");
	}

	public static double[] splitToDouble(String str, String spStr) {
		if (str.trim().length() == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}

		String[] temps = str.split(spStr);
		int len = temps.length;
		double[] results = new double[len];
		for (int i = 0; i < len; i++) {
			results[i] = Double.parseDouble(temps[i].trim());
		}
		return results;
	}

	public static void main(String[] str) {

	}
}
