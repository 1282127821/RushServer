package com.star.light.player;

/**
 * 钻石增加与删除类型
 */
public interface DiamondChangeType {

	// 1-999表示是增加的类型
	/**
	 * 无钻石
	 */
	short NONE = -1;

	/**
	 * GM命令获得
	 */
	short GM_GET_DIAMOND = 0;

	/**
	 * VIP月卡每日奖励
	 */
	short DAILYTASK_VIP_REWARD = 1;

	/**
	 * 副本结算奖励
	 */
	short CAMPAIGN_REWARD = 2;

	/**
	 * 抽奖获得
	 */
	short LUCKY_DRAW_ADD = 3;

	/**
	 * vip钻石包充值钻石数
	 */
	short VIP_CHARGE = 4;

	/**
	 * 从邮件附件中领取获得
	 */
	short MAIL_ATTACH = 5;

	/**
	 * 签到获得
	 */
	short SIGN = 6;

	/**
	 * 主线任务
	 */
	short MAIN_TASK = 7;

	/**
	 * 首冲奖励
	 */
	short FIRST_RECHARGE = 8;

	/**
	 * 礼包序列号兑换获得
	 */
	short EXCHANGE_CODE = 9;

	/**
	 * 使用礼包
	 */
	short USE_ITEM_BOX = 10;

	/**
	 * 7天活动
	 */
	short ACTIVITY_SEVEN = 11;

	/**
	 * 冲级活动
	 */
	short ACTIVITY_UPGRADE = 12;

	/**
	 * 日冲活动
	 */
	short ACTIVITY_DAY_CHARGE = 13;

	/**
	 * 月冲活动
	 */
	short ACTIVITY_WEEK_CHARGE = 14;

	/**
	 * 怪物攻城奖励
	 */
	short MONSTER_SIEGE_REWARD = 15;

	/**
	 * 离线竞技场奖励
	 */
	short ARENA_PK_REWARD = 16;

	/**
	 * 离线竞技场排名奖励
	 */
	short ARENA_PK_RANK_REWARD = 17;

	/**
	 * vip充值首次赠送
	 */
	short VIP_CHARGE_FIRST_DONATE = 18;

	/**
	 * vip充值额外赠送
	 */
	short VIP_CHARGE_EXTRA_DONATE = 19;

	/**
	 * 副本评级奖励
	 */
	short EVALUATION_REWARD = 20;

	/**
	 * 时空裂隙首通奖励
	 */
	short CRANNY_FIRST_REWARD = 21;

	/**
	 * 时空裂隙通关奖励
	 */
	short CRANNY_REWARD = 22;

	/**
	 * 新手奖励
	 */
	short GUIDE_REWARD = 23;

	/**
	 * 排位赛奖励
	 */
	short QUALIFYING = 24;

	/**
	 * 充值赠送
	 */
	short CHARGE_DONATE = 25;

	/**
	 * 世界boss杀死怪物
	 */
	short WORLD_KILL_MONSTER = 26;

	/**
	 * 世界boss杀死boss
	 */
	short WORLD_KILL_BOSS = 27;

	/**
	 * 世界boss排名奖
	 */
	short WORLD_RANK = 28;

	/**
	 * 世界boss伤害奖
	 */
	short WORLD_HURT = 29;

	/**
	 * 中控发送
	 */
	short CENTRAL_CONTROL = 30;

	/**
	 * 离线pk历史最高奖励
	 */
	short ARENA_HISTORY_RANK_REWARD = 31;

	/**
	 * vip礼包购买
	 */
	short VIP_BUY_PACKAGE = 32;
	/**
	 * 任务奖励
	 */
	short MISSION_REWARD = 33;

	/**
	 * 副本sss奖励
	 */
	short SSS_REWARD = 34;

	/**
	 * 神秘商店购买获得
	 */
	short BUYFROMMYSTERYSHOP = 35;

	/**
	 * 活动成长领取
	 */
	short ACTIVITY_GROW_FOUND = 36;

	/**
	 * 消耗活动领取
	 */
	short ACTIVITY_CONSUME = 37;

	/**
	 * 幸运抽盘活动领取
	 */
	short ACTIVITY_LUCK_ROLLER = 38;

	// 1000以上表示是消耗的类型
	/**
	 * 重置技能天赋消耗
	 */
	short RESET_SKILL_TALENT = 1000;

	/**
	 * 一键扫荡日常副本消耗
	 */
	short ONEKEY_SWEEP_DAILY_CAMPAIGN = 1001;

	/**
	 * 装备洗练
	 */
	short EQUIP_MACERATOR = 1002;

	/**
	 * 开启装备宝石孔位消耗
	 */
	short OPEN_EQUIPBONE_INLAY = 1003;

	/**
	 * 购买体力
	 */
	short BUY_ENERGY = 1004;

	/**
	 * 购买神秘商店商品消耗
	 */
	short BUY_MYSTERYSHOP_PROP = 1005;

	/**
	 * 购买使用回复药次数消耗
	 */
	short BUY_RECOVER_PROP_TIME = 1006;

	/**
	 * 各类副本内请求复活自己消耗
	 */
	short CAMPAIGN_REBORN_PLAYER = 1007;

	/**
	 * 创建公会消耗
	 */
	short CREATE_GUILD = 1008;

	/**
	 * BOSS场景中请求复活自己消耗
	 */
	short BOSS_SCENE_REBORN_PLAYER = 1009;

	/**
	 * 竞技场购买挑战次数
	 */
	short ARENA_BUY_CHALLEGE_TIMES = 1010;

	/**
	 * 摇钱树中兑换金币消耗
	 */
	short EXCHANGE_MONEY = 1011;

	/**
	 * 转职
	 */
	short TRANSFER = 1012;

	/**
	 * 玩家装备突破消耗
	 */
	short EQUIP_ADVANCE = 1013;

	/**
	 * 竞技场移除冷却时间
	 */
	short ARENA_BUY_REMOVE_COOL_TIME = 1014;

	/**
	 * 多人副本复活
	 */
	short MUL_INSTANCE_RESTORE = 1015;

	/**
	 * 多人副本购买奖励次数
	 */
	short MUL_INSTANCE_BUY = 1016;

	/**
	 * 玩家解锁技能链的槽位消耗
	 */
	short UNLOCK_SKILLCHAINSLOT = 1017;

	/**
	 * 手动刷新神秘商店的商品消耗
	 */
	short MANUAL_REFRESHMYSTERYSHOP = 1018;

	/**
	 * 多人副本使用道具
	 */
	short MUL_INSTANCE_USE_PROP = 1019;

	/**
	 * 召唤抽奖消耗
	 */
	short LUCKY_DRAW_DEL = 1020;

	/**
	 * 副本中钻石抽奖
	 */
	short CAMPAIGN_DIAMOND_LOTTYER = 1021;

	/**
	 * 主线精英副本限制次数重置钻石消耗
	 */
	short ELITE_CAMPAIGN_ATTACK_NUM_RESET = 1022;

	/**
	 * 公会钻石捐献消耗
	 */
	short GUILD_DIAMOND_DONATE = 1023;

	/**
	 * 生命之树升级
	 */
	short TOL_UPGRADE = 1024;

	/**
	 * 卡牌更换副本
	 */
	short CARD_CHANGE_INSTANCE = 1025;

	/**
	 * 卡牌复活
	 */
	short CARD_RESTORE = 1026;

	/**
	 * 卡牌再次挑战
	 */
	short CARD_RECHANGE = 1027;

	/**
	 * 天使消耗
	 */
	short ANGEL_COST = 1028;

	/**
	 * 时装购买消耗
	 */
	short FASHION_BUY_COST = 1029;

	/**
	 * 时空裂隙扫荡消耗
	 */
	short CRANNY_SWEEP = 1030;

	/**
	 * 世界boss复活
	 */
	short WORLD_BOSS_RESTORE = 1031;

	/**
	 * 装备碎片合成消耗
	 */
	short EQUIP_CHIP_SYNTHETISE_COST = 1032;

	/**
	 * 幻兽解锁
	 */
	short BEAST_UNLOCK = 1033;

	/**
	 * 购买vip礼包花费
	 */
	short VIP_BUY_PACKAGE_COST = 1034;

	/**
	 * 生命之树加速
	 */
	short TOL_ACCELERATE = 1035;

	/**
	 * 装备重铸
	 */
	short EQUIP_RECOIN = 1036;
	
	/**
	 * 公会守卫战侦查
	 */
	short GUILD_DETECT = 1037;
	
	/**
	 * 公会守卫战战斗
	 */
	short GUILD_FIGHT = 1038;
	
	/**
	 * 翅膀强化消耗
	 */
	short WING_STRONG = 1039;

	/**
	 * 翅膀升阶消耗
	 */
	short WING_UPGRADE_RANK = 1040;
}

