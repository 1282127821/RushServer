package com.player;

import com.prop.EquipType;

public class Property {
	/**
	 * 作为需要更新数据的标记
	 */
	private boolean isChange;

	/**
	 * 总和值
	 */
	private int totalJoin;

	/**
	 * 总万分比加成
	 */
	private int totalRate;

	/**
	 * 装备的属性值
	 */
	private int[] equipData;

	/**
	 * 装备的总属性
	 */
	private int totalEquipData;

	/**
	 * 等级的属性加成
	 */
	private int levelData;
	
	/**
	 * 角色的初始HP
	 */
	private int charHP;

	public Property() {
		equipData = new int[EquipType.COUNT];
	}

	private void fresh() {
		int  totalNoRateVal = totalEquipData + levelData + charHP;
		totalJoin = (int) ((totalNoRateVal * 1.0 * totalRate * GameConst.TEN_THOUSAND_RATE) + totalNoRateVal);
		isChange = false;
	}

	public void setTotalJoin(int totalJoin) {
		this.totalJoin = totalJoin;
	}

	public int getTotalJoin() {
		if (isChange)
			fresh();
		return totalJoin;
	}

	public void addEquipData(int index, int equipData) {
		if (this.equipData[index] != equipData) {
			this.equipData[index] = equipData;
			totalEquipData += equipData;
			isChange = true;
		}
	}

	public void clearEquipData(int index) {
		totalEquipData -= this.equipData[index];
		addEquipData(index, 0);
	}

	public void setLevelData(int levelData) {
		this.levelData = levelData;
		isChange = true;
	}
	
	public void setCharacterHP(int charHP) {
		this.charHP = charHP;
		isChange = true;
	}
}
