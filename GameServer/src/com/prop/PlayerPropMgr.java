package com.prop;

import java.util.ArrayList;
import java.util.List;

import com.BaseServer;
import com.db.DBOption;
import com.mail.MailType;
import com.pbmessage.GamePBMsg.ItemChangeListMsg;
import com.pbmessage.GamePBMsg.ItemInfoMsg;
import com.player.DaoMgr;
import com.player.GamePlayer;
import com.player.ItemChangeType;
import com.protocol.Protocol;
import com.table.ItemTemplateMgr;
import com.table.ResourceInfo;
import com.util.Log;
import com.util.TimeUtil;

public class PlayerPropMgr {
	private int packUpPackageTime = 0;
	private GamePlayer player;
	private PropInventory[] aryPropInventory;

	public PlayerPropMgr(GamePlayer player) {
		this.player = player;
		aryPropInventory = new PropInventory[BagType.COUNT];
		for (int index = BagType.EQUIP_FENCE; index < BagType.COUNT; ++index) {
			int bagCount = EquipType.COUNT;
			if (index == BagType.PACKAGE) {
				bagCount = 40;
			}
			aryPropInventory[index] = new PropInventory(index, bagCount, player);
		}
	}

	/**
	 * 从数据库中加载道具
	 */
	public void loadFromDB() {
		long userId = player.getUserId();
		List<PropInstance> props = DaoMgr.itemInfoDao.getAllItemInfo(userId);
		ItemChangeListMsg.Builder itemChangeList = ItemChangeListMsg.newBuilder();
		if (props != null && props.size() > 0) {
//			ItemChangeListMsg.Builder itemChangeList = ItemChangeListMsg.newBuilder();
			for (PropInstance propInfo : props) {
				PropInventory propInventory = aryPropInventory[propInfo.getBagType()];
				propInventory.init(new Prop(propInfo, null));
//				ItemInfoMsg.Builder itemMsg = ItemInfoMsg.newBuilder();
//				Prop.writePropInfo(propInfo, itemMsg);
//				itemChangeList.addItemChangeList(itemMsg);
			}
		} else {
			int[] aryTestPropId = new int[] { 11001, 11002, 11003, 11004, 11005, 11006, 11007, 11008, 11009, 11010, 11011, 11012, 11013, 11014, 11015, 11016};
			int[] aryTestMaterial = new int[] { 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31009, 31010, 31011, 31012, 31013 };

			for (int i = 0; i < aryTestPropId.length; i++) {
				addOnePropToPackage(aryTestPropId[i], 1, ItemChangeType.GMGET);
			}
			
			for (int i = 0; i < aryTestMaterial.length; i++) {
				addOnePropToPackage(aryTestMaterial[i], 50, ItemChangeType.GMGET);
			}
		}

		for (int index = BagType.EQUIP_FENCE; index < BagType.COUNT; ++index) {
			Prop[] aryTotalProp = aryPropInventory[index].getAllProp();
			for (int i = 0; i < aryTotalProp.length; i++) {
				Prop propInfo = aryTotalProp[i];
				if (propInfo != null) {
					ItemInfoMsg.Builder itemMsg = ItemInfoMsg.newBuilder();
					Prop.writePropInfo(propInfo.getPropInstance(), itemMsg);
					itemChangeList.addItemChangeList(itemMsg);
				}
			}
		}
		player.sendPacket(Protocol.S_C_ITEM_INIT, itemChangeList);
	}

	/**
	 * 根据类型获得相应的存储空间
	 */
	public PropInventory getPropInventory(int inventoryType) {
		return inventoryType < BagType.COUNT ? aryPropInventory[inventoryType] : null;
	}

	/**
	 * 获得对应存储空间的所有道具
	 */
	public Prop[] getAllProp(int inventoryType) {
		PropInventory propInventory = getPropInventory(inventoryType);
		return propInventory != null ? propInventory.getAllProp() : null;
	}

	/**
	 * 扩展存储空间的容量
	 */
	public void expandPackageCount(int inventoryType, int addCount) {
		getPropInventory(inventoryType).expandPackageCount(addCount);
	}

	/**
	 * 根据道具所属存储空间类型和位置获得相应的道具
	 */
	public Prop getPropByPosIndex(int inventoryType, int posIndex) {
		PropInventory propInventory = getPropInventory(inventoryType);
		return propInventory != null ? propInventory.getPropByPosIndex(posIndex) : null;
	}

	/**
	 * 根据玩家道具id 获取玩家道具信息，这个是需要遍历整个背包的，如果可以最好使用道具所在格子来使用
	 */
	public Prop getPropByItemId(int bagType, long itemId) {
		PropInventory propInventory = getPropInventory(bagType);
		return propInventory != null ? propInventory.getPropByItemId(itemId) : null;
	}

	/**
	 * 找到第一个为空的存储空间的位置
	 */
	public int getFirstFreeGrid(int inventoryType) {
		PropInventory propInventory = getPropInventory(inventoryType);
		return propInventory != null ? propInventory.getFirstFreeGrid() : Integer.MAX_VALUE;
	}

	/**
	 * 获得存储空间剩余的空的格子数量
	 */
	public int getFreeGridCount(int inventoryType) {
		PropInventory propInventory = getPropInventory(inventoryType);
		return propInventory != null ? propInventory.getFreeGridCount() : 0;
	}

	/**
	 * 根据道具位置和需要删除的数量进行删除道具
	 */
	public boolean destroyPropByProp(int inventoryType, Prop prop, int count, short itemChangeType) {
		return getPropInventory(inventoryType).destroyPropByProp(prop, count, itemChangeType);
	}

	/**
	 * 删除多个物品
	 */
	public boolean removeProp(List<ResourceInfo> removeList, short itemChangeType) {
		if (removeList == null || removeList.size() == 0) {
			return false;
		}

		// 检查数量是否足够
		for (int i = 0; i < removeList.size(); i++) {
			ResourceInfo info = removeList.get(i);
			int inventoryType = BagType.PACKAGE;
			PropInventory propInventory = getPropInventory(inventoryType);
			int bagCount = propInventory.getSpecificPropCount(info.resourceId);
			if (info.count > bagCount) {
				Log.error("removePropByPropId多个 error. userId : " + player.getUserId() + " , propId : " + info.resourceId + ", castCount : " + info.count + ", inventoryType : " + inventoryType
						+ ", bagCount : " + bagCount + ", itemChangeType : " + itemChangeType);
				return false;
			}
		}

		// 删除道具
		for (int i = 0; i < removeList.size(); i++) {
			ResourceInfo info = removeList.get(i);
			removePropByPropId(info.resourceId, info.count, itemChangeType);
		}
		return true;
	}

	/**
	 * 根据道具模板id 移除指定数量的道具
	 */
	public boolean removePropByPropId(int propId, int castCount, short itemChangeType) {
		int bagType = BagType.PACKAGE;
		PropInventory propInventory = getPropInventory(bagType);
		boolean isSuccess = propInventory.removePropByPropId(propId, castCount, itemChangeType);
		if (!isSuccess) {
			Log.error("removePropByPropId error. userId : " + player.getUserId() + " , propId : " + propId + " , castCount : " + castCount + " , bagType : " + bagType + ", itemChangeType : "
					+ itemChangeType);
		}

		return isSuccess;
	}

	/**
	 * ps:相同的道具外面要积累 根据道具模板id 移除指定数量的道具
	 */
	public boolean removePropByPropId(int inventoryType, int[] propIds, int[] castCounts, short itemChangeType) {
		PropInventory propInventory = getPropInventory(inventoryType);
		if (propInventory != null) {
			for (int i = 0; i < propIds.length; i++) {
				int bagCount = propInventory.getSpecificPropCount(propIds[i]);
				if (castCounts[i] > bagCount) {
					Log.error("removePropByPropId多个 error. userId : " + player.getUserId() + " , propId : " + propIds[i] + " , castCount : " + castCounts[i] + " , inventoryType : " + inventoryType
							+ "bagCount " + bagCount + ", itemChangeType : " + itemChangeType);
					return false;
				}
			}
			for (int i = 0; i < propIds.length; i++) {
				propInventory.removePropByPropId(propIds[i], castCounts[i], itemChangeType);
			}
		}
		return true;
	}

	/**
	 * 交换两个装备的操作的，上层必须保证srcType和destType是不一样的，因为端游之前是有可以一样的，手游没有，简化操作
	 */
	public boolean exchangeEquip(int srcType, int srcIndex, int destType, int destIndex) {
		PropInventory srcInventory = getPropInventory(srcType);
		Prop srcProp = srcInventory.getPropByPosIndex(srcIndex);
		if (srcProp == null) {
			return false;
		}

		if (destType == BagType.EQUIP_FENCE) {
			if (!checkCanEquip(srcProp, destIndex)) {
				return false;
			}
		}

		PropInventory destInventory = getPropInventory(destType);
		Prop destProp = destInventory.getPropByPosIndex(destIndex);
		if (destProp == null) {
			if (srcInventory.changePropByIndex(srcIndex)) {
				return destInventory.addPropByEmptyPos(srcProp, destIndex);
			}
		}

		if (srcType != destType) {
			if (srcInventory.changePropByIndex(srcIndex)) {
				if (srcInventory.addPropByEmptyPos(destProp, srcIndex)) {
					if (destInventory.changePropByIndex(destIndex)) {
						return destInventory.addPropByEmptyPos(srcProp, destIndex);
					}
				}
			}
			return false;
		}

		return true;
	}

	/**
	 * 检测是否能够穿上此装备
	 */
	private boolean checkCanEquip(Prop prop, int destIndex) {
		return prop.getEquipIndex() == destIndex && prop.getItemTempInfo().isEquip() && checkPropCanBeUse(prop);
	}

	/**
	 * 判断道具是否可以使用
	 */
	private boolean checkPropCanBeUse(Prop prop) {
		PropTemplate propTempInfo = prop.getItemTempInfo();
		if (propTempInfo.itemLv > player.getPlayerLv()) {
			return false;
		}

		int jobType = propTempInfo.job;
		if (jobType != 0 && jobType != player.getJobId()) {
			return false;
		}

		return true;
	}

	/**
	 * 这里只能够针对装备和道具两个，其它资源的不允许调用这里，需要上层自己做区分
	 */
	public void addPropToPackageOrMail(List<ResourceInfo> resourceInfo, short addType) {
		List<ResourceInfo> mailAttach = new ArrayList<ResourceInfo>(); // 这个是背包不足的时候添加到邮件上的
		for (ResourceInfo info : resourceInfo) {
			PropTemplate propTemplate = ItemTemplateMgr.getInstance().getItemTempInfo(info.resourceId);
			if (propTemplate == null) {
				Log.error("propTemp is null. userId : " + player.getUserId() + ", tempId : " + info.resourceId);
				continue;
			}
			
			addPropCommon(BagType.PACKAGE, mailAttach, info, addType);
		}

		if (mailAttach.size() > 0) {
			// 发送邮件道具
			player.getMailMgr().addMail(MailType.SYSTEM, "通关奖励", "背包空间不足，发送至邮件", mailAttach);
		}
	}
	
	/**
	 * 上层传入一个Resource的列表（里面包括有资源，装备、物品这三种）,返回false表示格子不足，返回true表示成功，不走邮件通道
	 */
	public boolean addResourceToPackage(List<ResourceInfo> rewardResources, short itemChange, short diamondChange) {
		List<ResourceInfo> resourceList = new ArrayList<ResourceInfo>();
		List<ResourceInfo> propList = new ArrayList<ResourceInfo>();
		for (ResourceInfo attach : rewardResources) {
			int resourceId = attach.resourceId;
			if (resourceId <= ItemTemplateId.TOTALNUM) {
				resourceList.add(attach);
			} else {
				propList.add(attach);
			}
		}

		// 检查背包是否有足够的格子
		int freeCount = getFreeGridCount(BagType.PACKAGE);
		if (freeCount < propList.size()) {
			return false;
		}

		addPropListToPackage(BagType.PACKAGE, propList, itemChange);
		for (ResourceInfo attach : resourceList) {
			player.addResource(attach.resourceId, attach.count, diamondChange, itemChange);
		}
		return true;
	}
	
	/**
	 * 往存储空间里面添加一个物品, 外部需要保证这里的数量保证传进来是不能够大于自身的堆叠数量。有需要添加超过自身堆叠数量的，
	 * 请外部逻辑自行处理 另外这个调用创建后的道具或者装备一定是保证能够添加到存储空间的，统计的时候需要用到。
	 */
	public Prop addOnePropToPackage(int propId, int count, short addType) {
		PropTemplate propTemplate = ItemTemplateMgr.getInstance().getItemTempInfo(propId);
		if (propTemplate == null) {
			Log.error("CreateProp失败，道具Id不存在. 道具Id为:  " + propId + "    道具数量为:  " + count + "    道具操作类型为:  " + addType);
			return null;
		}

		PropInstance newPropInstance = new PropInstance();
		newPropInstance.setId(BaseServer.IDWORK.nextId());
		newPropInstance.setTemplateId(propId);
		newPropInstance.setStackCount(count);
		newPropInstance.setGainTime(TimeUtil.getSysCurSeconds());
		newPropInstance.setPosIndex(-1);
		newPropInstance.setOp(DBOption.INSERT);
	
		if (propTemplate.isEquip()) {
			newPropInstance.createEquipInstance();
		}

		Prop prop = new Prop(newPropInstance, propTemplate);
		boolean isSuccess = getPropInventory(BagType.PACKAGE).addOnePropToPackage(prop, addType);
		if (!isSuccess) {
			Log.error("添加道具到背包中失败，是不是没有判断背包格子足够. UserId: " + player.getUserId() + "");
			return null;
		}
		
		return prop;
	}

	private void addPropCommon(int bagType, List<ResourceInfo> mailAttach, ResourceInfo info, short addType) {
		int freeCount = getPropInventory(bagType).getFreeGridCount();
		if (freeCount == 0) {
			mailAttach.add(info);
		} else {
			addOnePropToPackage(info.resourceId, info.count, addType);
		}
	}

	/**
	 * 添加多个物品到对应的存储空间
	 */
	public void addPropListToPackage(int inventoryType, List<ResourceInfo> propList, short addType) {
		PropInventory propInventory = getPropInventory(inventoryType);
		if (propInventory != null && propList.size() > 0) {
			propInventory.beginChanges();
			for (ResourceInfo info : propList) {
				addOnePropToPackage(info.resourceId, info.count, addType);
			}
			propInventory.commitChanges();
		}
	}

	/**
	 * 添加多个物品或者资源
	 */
	public boolean addPropOrRes(List<ResourceInfo> resourceList, short addItemType, short addDiamondType) {
		List<ResourceInfo> addPropList = null;
		for (int i = 0; i < resourceList.size(); i++) {
			ResourceInfo info = resourceList.get(i);
			int resourceId = info.resourceId;
			if (resourceId <= ItemTemplateId.TOTALNUM) {
				player.addResource(resourceId, info.count, addDiamondType, addItemType);
			} else {
				if (addPropList == null) {
					addPropList = new ArrayList<ResourceInfo>();
				}
				addPropList.add(info);
			}
		}

		if (addPropList != null) {
			addPropToPackageOrMail(addPropList, addItemType);
		}
		return true;
	}

	/**
	 * 添加超过自身最大堆叠数量的操作
	 */
	public void addPropToPackage(int propId, int count, short addType) {
		PropTemplate propTemplate = ItemTemplateMgr.getInstance().getItemTempInfo(propId);
		int maxStackCount = propTemplate.maxStackCount;
		int curNum = count / maxStackCount;
		int leftNum = count - curNum * maxStackCount;
		for (int i = 0; i < curNum; i++) {
			player.getPropMgr().addOnePropToPackage(propId, maxStackCount, addType);
		}

		if (leftNum != 0) {
			player.getPropMgr().addOnePropToPackage(propId, leftNum, addType);
		}
	}

	/**
	 * 自动整理物品背包
	 */
	public void autoPackUpSort() {
		int curSeconds = TimeUtil.getSysCurSeconds();
		if (curSeconds < packUpPackageTime + 3) {
			return;
		}

		packUpPackageTime = curSeconds;
		PropInventory packageInventory = getPropInventory(BagType.PACKAGE);
		packageInventory.beginChanges();
		packageInventory.autoPackUpSort();
		packageInventory.commitChanges();
	}
	
	/**
	 * 卸载内存数据
	 */
	public void unloadData() {
		for (int index = BagType.EQUIP_FENCE; index < BagType.COUNT; ++index) {
			aryPropInventory[index] = null;
		}
	}

	/**
	 * 存储到数据库中
	 */
	public void saveToDB() {
		try {
			long userId = player.getUserId();
			for (int index = BagType.EQUIP_FENCE; index < BagType.COUNT; ++index) {
				aryPropInventory[index].saveToDB(userId);
			}
		} catch (Exception e) {
			Log.error("保存玩家道具信息出错, UserId:  " + player.getUserId(), e);
		}
	}
}
