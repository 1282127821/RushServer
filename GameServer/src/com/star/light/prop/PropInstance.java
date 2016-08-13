package com.star.light.prop;

import com.star.light.db.DBObject;
import com.star.light.db.DBOption;
import com.star.light.player.PropertyInfo;
import com.star.light.util.SplitUtil;

public class PropInstance extends DBObject {
	/**
	 * 数据库主键编号
	 */
	private long itemId;

	/**
	 * 道具的模板Id
	 */
	private int propTemplateId;

	/**
	 * 位置
	 */
	private int posIndex;

	/**
	 * 堆叠数量
	 */
	private int stackCount;

	/**
	 * 存储空间类型
	 */
	private int bagType;

	/**
	 * 道具获得的时间
	 */
	private int gainTime;

	/**
	 * 装备字段专有
	 */
	private EquipInstance equipInstance;

	public void createEquipInstance() {
		this.equipInstance = new EquipInstance();
	}

	public EquipInstance getEquipInstance() {
		return equipInstance;
	}

	public void setId(long itemId) {
		this.itemId = itemId;
	}

	public long getId() {
		return this.itemId;
	}

	public void setTemplateId(int propId) {
		if (this.propTemplateId != propId) {
			this.propTemplateId = propId;
			setOp(DBOption.UPDATE);
		}
	}

	public int getTemplateId() {
		return this.propTemplateId;
	}

	public void setPosIndex(int posIndex) {
		if (this.posIndex != posIndex) {
			this.posIndex = posIndex;
			setOp(DBOption.UPDATE);
		}
	}

	public int getPosIndex() {
		return this.posIndex;
	}

	public void setStackCount(int count) {
		if (this.stackCount != count) {
			this.stackCount = count;
			setOp(DBOption.UPDATE);
		}
	}

	public int getStackCount() {
		return this.stackCount;
	}

	public void setBagType(int bagType) {
		if (this.bagType != bagType) {
			this.bagType = bagType;
			setOp(DBOption.UPDATE);
		}
	}

	public int getBagType() {
		return this.bagType;
	}

	public int getGainTime() {
		return gainTime;
	}

	public void setGainTime(int gainTime) {
		if (this.gainTime != gainTime) {
			this.gainTime = gainTime;
			setOp(DBOption.UPDATE);
		}
	}

	public int getBlessAttribute() {
		return equipInstance != null ? equipInstance.blessAttribute : 0;
	}

	public void setBlessAttribute(int blessAttribute) {
		equipInstance.blessAttribute = blessAttribute;
		setOp(DBOption.UPDATE);
	}
	
	public int getBlessLv() {
		return equipInstance != null ? equipInstance.blessLv : 0;
	}

	public void setBlessLv(int blessLv) {
		equipInstance.blessLv = blessLv;
		setOp(DBOption.UPDATE);
	}

	public String getStrInlay() {
		int[] aryInlay = getAryInlay();
		StringBuilder sb = new StringBuilder();
		for (int cardId : aryInlay) {
			sb.append(cardId).append(",");
		}
		return sb.toString();
	}

	public void setStrInlay(String inlayStr) {
		if (!inlayStr.equals("")) {
			String[] aryInlay = inlayStr.split(",");
			int len = equipInstance.aryInlay.length;
			for (int i = 0; i < len; i++) {
				equipInstance.aryInlay[i] = Integer.parseInt(aryInlay[i]);
			}
		}
	}

	public int[] getAryInlay() {
		return equipInstance != null ? equipInstance.aryInlay : SplitUtil.EMPTY_INT_ARRAY;
	}

	public void setAryInlay(int[] aryInlay) {
		equipInstance.aryInlay = aryInlay;
		setOp(DBOption.UPDATE);
	}

	public String getStrAttribute() {
		PropertyInfo[] aryAttribute = getAttribute();
		StringBuilder sb = new StringBuilder();
		for (PropertyInfo info : aryAttribute) {
			sb.append(info.type).append(":").append(info.value).append(",");
		}
		return sb.toString();
	}

	public void setStrAttribute(String attributeStr) {
		if (!attributeStr.equals("")) {
			String[] aryAttribute = attributeStr.split(",");
			int len = aryAttribute.length;
			equipInstance.aryAttribute = new PropertyInfo[len];
			for (int i = 0; i < len; i++) {
				String[] aryInfo = aryAttribute[i].split(":");
				equipInstance.aryAttribute[i] = new PropertyInfo(Integer.parseInt(aryInfo[0]), Integer.parseInt(aryInfo[1]));
			}
		} else {
			equipInstance.aryAttribute = PropertyInfo.EMPTY_PROPERTYINFO;
		}
	}

	public PropertyInfo[] getAttribute() {
		if (equipInstance == null || equipInstance.aryAttribute == null) {
			return PropertyInfo.EMPTY_PROPERTYINFO;
		}
		return equipInstance.aryAttribute;
	}

	public void setAttribute(PropertyInfo[] aryAttribute) {
		this.equipInstance.aryAttribute = aryAttribute;
		setOp(DBOption.UPDATE);
	}

	public int getFightstrength() {
		return equipInstance != null ? equipInstance.fightstrength : 0;
	}

	public void setFightstrength(int fightstrength) {
		equipInstance.fightstrength = fightstrength;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id:		").append(itemId).append("\n");
		sb.append("propId:	").append(propTemplateId).append("\n");
		sb.append("pos:		").append(posIndex).append("\n");
		sb.append("stackCount:		").append(stackCount).append("\n");
		sb.append("bagType:		").append(bagType).append("\n");
		sb.append("gainTime:		").append(gainTime).append("\n");
		sb.append("blessLv:		").append(getBlessLv()).append("\n");
		sb.append("blessAttribute:		").append(getBlessAttribute()).append("\n");
		sb.append("aryInlay:").append(getStrInlay()).append("\n");
		sb.append("aryAttribute:		").append(getStrAttribute()).append("\n");
		return sb.toString();
	}
}

class EquipInstance {
	/**
	 * 祝福等级
	 */
	public int blessLv;
	
	/**
	 * 祝福的属性，根据装备的基础属性来决定
	 */
	public int blessAttribute;
	
	/**
	 * 装备镶嵌列表
	 */
	public int[] aryInlay = new int[HoleState.HOLE_COUNT];

	/**
	 * 装备副属性
	 */
	public PropertyInfo[] aryAttribute;

	/**
	 * 装备战斗力，不保存数据库，只作为内存值使用
	 */
	public int fightstrength;
}