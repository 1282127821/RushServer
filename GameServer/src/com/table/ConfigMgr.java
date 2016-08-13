package com.table;

import java.util.HashMap;
import java.util.Map;

import com.file.CTabFile;

public final class ConfigMgr {
	private static ConfigMgr instance = new ConfigMgr();

	public static ConfigMgr getInstance() {
		return instance;
	}

	/**
	 * 背包格子最大上限
	 */
	public static int packageMaxGrid;

	/**
	 * 玩家的最大等级
	 */
	public static int playerMaxLv;

	/**
	 * 技能的最大等级
	 */
	public static int maxSkillLv;

	/**
	 * 技能升级所需要玩家等级增量
	 */
	public static int skillLvUpLimitAdd;

	/**
	 * 公会系统开放等级
	 */
	public static int openGuildLv;

	/**
	 * 创建公会需要花费的钻石数量
	 */
	public static int createGuildCost;

	/**
	 * 可以同时申请加入氏族的数量
	 */
	public static int maxGuildApplyCount;

	/**
	 * 公会人数基础数量
	 */
	public static int guildMemberBase;

	/**
	 * 公会人数随等级增加值
	 */
	public static int guildMemberLevelFactor;

	/**
	 * 进阶消耗基础值
	 */
	public static int equipAdvanceBase;

	/**
	 * 进阶消耗等级参数
	 */
	public static int equipAdvanceLevelFactor;

	/**
	 * 进阶消耗品级参数
	 */
	public static int equipAdvanceQualityFactor;

	/**
	 * 进阶材料概率递减百分比
	 */
	public static int equipAdvanceReducePercent;

	/**
	 * 镶嵌消耗基础值
	 */
	public static int equipInlayBase;

	/**
	 * 镶嵌消耗品级参数
	 */
	public static int equipInlayQualityFactor;

	/**
	 * 镶嵌卡槽1开启等级
	 */
	public static int equipInlayOneLv;

	/**
	 * 镶嵌卡槽2开启等级
	 */
	public static int equipInlayTwoLv;

	/**
	 * 镶嵌卡槽3开启等级
	 */
	public static int equipInlayThreeLv;

	/**
	 * 装备祝福消耗基础值
	 */
	public static int equipBlessBase;

	/**
	 * 装备祝福消耗等级参数
	 */
	public static double equipBlessLevelFactor;

	/**
	 * 装备祝福消耗品级参数
	 */
	public static int equipBlessQualityFactor;

	/**
	 * 材料2祝福成功率等级参数
	 */
	public static double equipBlessMaterialLevelFactor;

	/**
	 * 材料2祝福成功率品级参数
	 */
	public static double equipBlessMaterialQualityFactor;

	/**
	 * 材料2祝福成功率祝福等级参数
	 */
	public static double equipBlessLvFactor;

	/**
	 * 材料1成功率基础参数
	 */
	public static double equipBlessBaseRate;

	/**
	 * 材料1成功率增加参数
	 */
	public static double equipBlessAddRate;

	/**
	 * 装备祝福无效概率
	 */
	public static double equipBlessInvalidChance;

	/**
	 * 装备祝福失败概率
	 */
	public static double equipBlessFailChance;
	
	/**
	 * 达到此祝福等级后祝福额外增加属性
	 */
	public static int equipBlessOverLv;
	
	/**
	 * 装备祝福的最高等级
	 */
	public static int equipBlessMaxLv;

	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		int len = file.getRows();
		Map<String, String> configMap = new HashMap<String, String>(len);
		for (int i = 0; i < len; ++i) {
			String key = file.getStringByColName(i, "ConfigName");
			String value = file.getStringByColName(i, "ConfigValue");
			configMap.put(key, value);
		}

		packageMaxGrid = Integer.parseInt(configMap.get("ItemBackPackGridMax"));
		openGuildLv = Integer.parseInt(configMap.get("clancreatlevel"));
		maxGuildApplyCount = Integer.parseInt(configMap.get("clanapplymax"));
		createGuildCost = Integer.parseInt(configMap.get("clancreatcost"));
		guildMemberBase = Integer.parseInt(configMap.get("clanmemberbase"));
		guildMemberLevelFactor = Integer.parseInt(configMap.get("clanmemberbylevel"));
		playerMaxLv = Integer.parseInt(configMap.get("MaxLevel"));
		maxSkillLv = Integer.parseInt(configMap.get("SkillLevelMax"));
		equipAdvanceBase = Integer.parseInt(configMap.get("advancecostbase"));
		equipAdvanceLevelFactor = Integer.parseInt(configMap.get("advancecostperlevel"));
		equipAdvanceQualityFactor = Integer.parseInt(configMap.get("advancecostperquality"));
		equipAdvanceReducePercent = Integer.parseInt(configMap.get("advancematerialreduce"));
		equipInlayBase = Integer.parseInt(configMap.get("setcostbase"));
		equipInlayQualityFactor = Integer.parseInt(configMap.get("setcostperquality"));
		equipInlayOneLv = Integer.parseInt(configMap.get("setpart1level"));
		equipInlayTwoLv = Integer.parseInt(configMap.get("setpart2level"));
		equipInlayThreeLv = Integer.parseInt(configMap.get("setpart3level"));
		equipBlessBase = Integer.parseInt(configMap.get("blesscostbase"));
		equipBlessLevelFactor = Double.parseDouble(configMap.get("blesscostperlevel"));
		equipBlessQualityFactor = Integer.parseInt(configMap.get("blesscostperquality"));
		equipBlessMaterialLevelFactor = Double.parseDouble(configMap.get("blessmaterial2level"));
		equipBlessMaterialQualityFactor = Double.parseDouble(configMap.get("blessmaterial2quality"));
		equipBlessLvFactor = Double.parseDouble(configMap.get("blessmaterial2blesslevel"));
		equipBlessBaseRate = Double.parseDouble(configMap.get("blessmaterial1baserate"));
		equipBlessAddRate = Double.parseDouble(configMap.get("blessmaterial1addrate"));
		equipBlessInvalidChance = Double.parseDouble(configMap.get("blessinvalid"));
		equipBlessFailChance = Double.parseDouble(configMap.get("blessfail"));
		equipBlessOverLv = Integer.parseInt(configMap.get("blesslevelforaddpro"));
		equipBlessMaxLv = Integer.parseInt(configMap.get("blesslevelmax"));
		return true;
	}
}