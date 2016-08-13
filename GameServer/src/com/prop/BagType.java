package com.prop;

/**
 * 物品所属背包类型
 */
public interface BagType {
	/**
	 * 表示新创建的物品的存储空间类型
	 */
	int NONE = -1;

	/**
	 * 装备栏
	 */
	int EQUIP_FENCE = 0;

	/**
	 * 物品背包
	 */
	int PACKAGE = 1;
	
	/**
	 * 用于统计有存储的个数,必须保证在最后一个,新增类型需要同步修改大小
	 */
	int COUNT = 2;
}
