package com.star.light.player;

public class PropertyInfo implements Comparable<PropertyInfo> {
	/**
	 * 类型
	 */
	public int type;

	/**
	 * 值
	 */
	public int value;

	public static final PropertyInfo[] EMPTY_PROPERTYINFO = new PropertyInfo[0];

	public PropertyInfo(int type, int value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public int compareTo(PropertyInfo info) {
		return this.value - info.value;
	}
}
