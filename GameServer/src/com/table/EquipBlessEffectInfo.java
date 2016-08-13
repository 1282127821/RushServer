package com.table;

public class EquipBlessEffectInfo {
	/**
	 * 物品的稀有度（品级）
	 */
	public int equipQuality;
	
	/**
	 * 安全等级，目标祝福等级低于或等于该值时，祝福不会失败
	 */
	public int safeLv;
	
	/**
	 * 物理攻击固定步长
	 */
	public int fixedAttack;
	
	/**
	 * 最小物理攻击值
	 */
	public int minDangerAttack;
	
	/**
	 * 最大物理攻击值
	 */
	public int maxDangerAttack;
	
	/**
	 * 物理攻击附加步长
	 */
	public int addAttack;
	
	/**
	 * 物理防御固定步长
	 */
	public int fixedDefence;
	
	/**
	 * 最小物理攻击值
	 */
	public int minDangerDefence;
	
	/**
	 * 最大物理防御值
	 */
	public int maxDangerDefence;
	
	/**
	 * 物理防御附加步长
	 */
	public int addDefence;
}
