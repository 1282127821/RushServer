package com.prop;

import com.player.PropertyInfo;

public class PropTemplate {
	/**
	 * 物品编号
	 */
	public int itemId;

	/**
	 * 物品名字
	 */
	public String itemName;

	/**
	 * 物品大类
	 */
	public int masterType;

	/**
	 * 物品小类
	 */
	public int sonType;

	/**
	 * 物品等级
	 */
	public int itemLv;

	/**
	 * 物品的排序
	 */
	public int itemOrder;

	/**
	 * 职业
	 */
	public int job;

	/**
	 * 最大叠加数量，默认值为1
	 */
	public int maxStackCount = 1;

	/**
	 * 装备品质
	 */
	public int quality;
	
	/**
	 * 属性值数组
	 */
	public PropertyInfo[] aryAttributeValue;

	/**
	 * 出售金币价格
	 */
	public int sellGold;

	/**
	 * 判断是否为装备
	 */
	public boolean isEquip() {
		return sonType >= EquipType.CASQUE && sonType <= EquipType.RIGHT_WEAPON;
	}
}