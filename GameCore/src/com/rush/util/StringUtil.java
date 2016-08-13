package com.rush.util;

public final class StringUtil {
	/**
	 * 判断字符串是否为 null 或者 空串
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断给定的字符为不为null和空串
	 */
	public static boolean isNotNullOrEmpty(String str) {
		return str != null && !"".equals(str);
	}

	/**
	 * 是否为数字类型(负数返回false)
	 */
	public static boolean isNumber(String str) {
		return str.matches("\\d*");
	}

	/**
	 * 通过ascii做非空判断
	 */
	public static boolean isContainsSpace(String str) {
		if (str == null) {
			return true;
		}

		char[] b = str.toCharArray();
		for (int i = 0; i < b.length; i++) {
			if ((int) b[i] == 32) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
	
	}
}
