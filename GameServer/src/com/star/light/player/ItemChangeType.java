package com.star.light.player;

/**
 * 资源、道具、装备改变类型
 */
public interface ItemChangeType {
	// 1-999表示是道具增加的类型
	short NONE = -1;

	/**
	 * GM命令获得
	 */
	short GMGET = 0;
	
	/**
	 * 副本通关奖励
	 */
	short LEVEL_STAGE_REWARD = 1;
	
	/**
	 * 副本通关之后的随机骰子奖励
	 */
	short PVP_RANDOM_DICE_REWARD = 2;
	
	/**
	 * 邮件附件奖励领取
	 */
	final short MAIL_ATTACH = 3;

	// 1000表示是道具消耗的类型
	/**
	 * 创建公会消耗
	 */
	short CREATE_GUILD = 1000;

	/**
	 * 装备进阶消耗
	 */
	short EQUIP_ADVANCE_COST= 1001;
	
	/**
	 * 装备镶嵌宝石升级消耗
	 */
	short EQUIP_BLESS_COST = 1002;

	/**
	 * 装备镶嵌宝石消耗
	 */
	short EQUIP_INLAY_COST = 1003;

	/**
	 * 出售道具消耗
	 */
	short SELL_PROP = 1004;

	/**
	 * 使用道具消耗
	 */
	short USE_PROP = 1005;

	/**
	 * 自动堆叠物品消耗
	 */
	short AUTO_STACK_COST = 1006;

	/**
	 * 升级技能消耗
	 */
	short UPGRADE_SKILL = 1007;
}
