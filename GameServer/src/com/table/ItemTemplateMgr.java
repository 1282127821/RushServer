package com.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.file.CTabFile;
import com.player.GameConst;
import com.player.PropertyInfo;
import com.prop.PropTemplate;
import com.prop.PropType;

public final class ItemTemplateMgr {
	/**
	 * 所有的物品和装备列表
	 */
	private Map<Integer, PropTemplate> propTemplateMap;
	
	/**
	 * 装备列表
	 */
	private List<PropTemplate> equipList;

	/**
	 * 宝石列表
	 */
	private List<PropTemplate> stoneList;

	private static ItemTemplateMgr instance = new ItemTemplateMgr();

	public static ItemTemplateMgr getInstance() {
		return instance;
	}

	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName))
			return false;

		int len = file.getRows();
		propTemplateMap = new HashMap<Integer, PropTemplate>(len);
		stoneList = new ArrayList<PropTemplate>();
		equipList = new ArrayList<PropTemplate>();
		for (int i = 0; i < len; ++i) {
			PropTemplate info = new PropTemplate();
			int itemId = file.getIntByColName(i, "ItemId");
			info.itemId = itemId;
			info.itemName = file.getStringByColName(i, "Name");
			int masterType = file.getIntByColName(i, "MasterType");
			info.masterType = masterType;
			info.sonType = file.getIntByColName(i, "SonType");
			info.itemLv = file.getIntByColName(i, "ItemLevel");
			info.itemOrder = file.getIntByColName(i, "ItemOrder");
			info.job = file.getIntByColName(i, "Job");
			info.maxStackCount = file.getIntByColName(i, "MaxCount");
			int quality = file.getIntByColName(i, "Quality");
			info.quality = quality;
			if (masterType == PropType.EQUIP || masterType == PropType.INLAY_CARD) {
				PropertyInfo[] aryAttributeValue = new PropertyInfo[GameConst.FIGHT_ATTRIBUTE_COUNT];
				aryAttributeValue[0] = new PropertyInfo(file.getIntByColName(i, "BaseProperty") - 1, file.getIntByColName(i, "BaseValue"));
				aryAttributeValue[1] = new PropertyInfo(file.getIntByColName(i, "AddProperty1") - 1, file.getIntByColName(i, "AddValue1"));
				aryAttributeValue[2] = new PropertyInfo(file.getIntByColName(i, "AddProperty2") - 1, file.getIntByColName(i, "AddValue2"));
				aryAttributeValue[3] = new PropertyInfo(file.getIntByColName(i, "AddProperty3") - 1, file.getIntByColName(i, "AddValue3"));
				aryAttributeValue[4] = new PropertyInfo(file.getIntByColName(i, "AddProperty4") - 1, file.getIntByColName(i, "AddValue4"));
				info.aryAttributeValue = aryAttributeValue;
			}
			
			if (masterType == PropType.EQUIP) {
				equipList.add(info);
			}
			
			propTemplateMap.put(itemId, info);
		}

		return true;
	}

	public PropTemplate getItemTempInfo(int tempId) {
		return propTemplateMap.get(tempId);
	}
	
	public PropTemplate getEquipAdvanceProp(int sonType, int quality) {
		for (PropTemplate propTemplate : equipList) {
			if (propTemplate.sonType == sonType && propTemplate.quality == quality) {
				return propTemplate;
			}
		}
		
		return null;
	}
	
	
	/**
	 * 获取宝石的Id
	 */
	public int getGemStoneId(int sonType, int stoneLv) {
		for (PropTemplate info : stoneList) {
			if (info.sonType == sonType && info.itemLv == stoneLv) {
				return info.itemId;
			}
		}
		return 0;
	}
}
