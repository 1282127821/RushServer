package com.star.light.prop;

import com.star.light.player.PropertyInfo;
import com.star.light.table.ItemTemplateMgr;

import tbgame.pbmessage.GamePBMsg.AttributeInfoMsg;
import tbgame.pbmessage.GamePBMsg.ItemInfoMsg;

public class Prop {
	/**
	 * 道具模板信息
	 */
	private PropTemplate propTemplate;

	/**
	 * 道具存储到数据库的信息
	 */
	private PropInstance propInstance;

	public Prop(PropInstance propInstance, PropTemplate propTemplate) {
		this.propInstance = propInstance;
		this.propTemplate = propTemplate == null ? ItemTemplateMgr.getInstance().getItemTempInfo(propInstance.getTemplateId()) : propTemplate;
	}

	public PropInstance getPropInstance() {
		return propInstance;
	}

	public PropTemplate getItemTempInfo() {
		return propTemplate;
	}
	
	public void setItemTempInfo(PropTemplate propTemplate) {
		this.propTemplate = propTemplate;
	}

	public int getPropTempId() {
		return propTemplate.itemId;
	}

	public int getBlessLv() {
		return propInstance.getBlessLv();
	}

	public void setBlessLv(int blessLv) {
		propInstance.setBlessLv(blessLv);
	}
	
	public int getBlessAttribute() {
		return propInstance.getBlessAttribute();
	}

	public void setBlessAttribute(int blessAttribute) {
		propInstance.setBlessAttribute(blessAttribute);
	}

	public int getPropLv() {
		return propTemplate.itemLv;
	}

	public int getPropOrder() {
		return propTemplate.itemOrder;
	}

	public int getMasterType() {
		return propTemplate.masterType;
	}
	
	public int getSonType() {
		return propTemplate.sonType;
	}

	public int getQuality() {
		return propTemplate.quality;
	}

	public int getFightStrength() {
		return propInstance.getFightstrength();
	}

	/**
	 * 根据装备的孔位获得对应孔位的卡片Id
	 */
	public int getInlayCardId(int inlayIndex) {
		int cardId = 0;
		int[] aryInlay = propInstance.getAryInlay();
		int index = inlayIndex - 1;
		if (index >= 0 && index < aryInlay.length) {
			cardId = aryInlay[index];
		}
		return cardId;
	}

	/**
	 * 根据装备的孔位和卡片Id对其进行镶嵌
	 */
	public void setInlayCardId(int inlayIndex, int cardId) {
		int[] aryInlay = propInstance.getAryInlay();
		int index = inlayIndex - 1;
		if (index >= 0 && index < aryInlay.length) {
			aryInlay[index] = cardId;
			propInstance.setAryInlay(aryInlay);
		}
	}
	
	/**
	 * 是否有镶嵌卡片
	 */
	public boolean isInlayCard(int inlayIndex) {
		return getInlayCardId(inlayIndex) > HoleState.NOTINLAY;
	}

	public int getMaxStackCount() {
		return propTemplate.maxStackCount;
	}

	/**
	 * 判断物品是否可以叠加
	 */
	public boolean isCanStack() {
		return propTemplate.maxStackCount > 1;
	}

	public int getPosIndex() {
		return propInstance.getPosIndex();
	}

	public void setPosIndex(int posIndex) {
		propInstance.setPosIndex(posIndex);
	}

	public void setStackCount(int stackCount) {
		propInstance.setStackCount(stackCount);
	}

	public int getStackCount() {
		return propInstance.getStackCount();
	}

	/**
	 * 根据装备的父类型获得该装备的索引位置
	 */
	public int getEquipIndex() {
		return propTemplate.sonType - 1;
	}

	/**
	 * 单个道具信息
	 */
	public static void writePropInfo(PropInstance propInstance, ItemInfoMsg.Builder propMsg) {
		propMsg.setItemId(propInstance.getId());
		propMsg.setTemplateId(propInstance.getTemplateId());
		propMsg.setPosIndex(propInstance.getPosIndex());
		propMsg.setStackCount(propInstance.getStackCount());
		propMsg.setGainTime(propInstance.getGainTime());
		propMsg.setBagType(propInstance.getBagType());
		propMsg.setBlessLv(propInstance.getBlessLv());
		propMsg.setBlessAttribute(propInstance.getBlessAttribute());
		for (int cardId : propInstance.getAryInlay()) {
			propMsg.addInlayCard(cardId);
		}
		PropertyInfo[] aryAttribute = propInstance.getAttribute();
		for (PropertyInfo info : aryAttribute) {
			AttributeInfoMsg.Builder infoMsg = AttributeInfoMsg.newBuilder();
			infoMsg.setAttributeType(info.type);
			infoMsg.setAttributeValue(info.value);
			propMsg.addAttributeInfo(infoMsg);
		}
	}

	/**
	 * 计算装备的战斗力
	 */
	public void calcEquipFight() {

	}
}
