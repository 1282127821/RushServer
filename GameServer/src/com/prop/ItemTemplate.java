package com.prop;

public class ItemTemplate {
	/**
	 * 物品的Id
	 */
	public int itemId;
	
	/**
	 * 物品的名字
	 */
	public String itemName;

	/**
	 * 物品的大类
	 */
	public int bigType;

	/**
	 * 物品的小类
	 */
	public int smallType;

	/**
	 * 物品使用等级
	 */
	public int useLv;
	
	/**
	 * 购买的基础价格
	 */
	public int buyPrice;
	
	/**
	 * 出售的基础价格
	 */
	public int sellPrice;
	
	/**
	 * 所需的职业
	 */
	public int needJob;
	
	/**
	 * 装备品质
	 */
	public int quality;
	
	/**
	 * 最大叠加数量，默认值为1
	 */
	public int maxStackCount;
	
	/**
	 * 装备单独特有的属性
	 */
	public EquipTemplate equipTemplate;
}
