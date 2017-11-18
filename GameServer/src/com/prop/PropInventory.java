package com.prop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.db.DBOption;
import com.pbmessage.GamePBMsg.ItemChangeListMsg;
import com.pbmessage.GamePBMsg.ItemInfoMsg;
import com.player.DaoMgr;
import com.player.GamePlayer;
import com.player.ItemChangeType;
import com.protocol.Protocol;
import com.util.Log;

/**
 * 道具存储空间，比如装备栏、装备背包、背包等
 */
public class PropInventory {
	/**
	 * 存储空间的类型
	 */
	private int inventoryType;

	/**
	 * 道具存储列表
	 */
	private Prop[] aryProp;

	private GamePlayer player;

	private Object lock = new Object();

	protected List<Integer> changedPlaces;
	private AtomicInteger changeCount = new AtomicInteger(0);

	/**
	 * 用于表示存储空间的大小，存储空间的容量
	 */
	private int gridCount;

	/**
	 * 拥有的数量
	 */
	private int propCount = 0;

	/**
	 * 要被删除掉的道具列表
	 */
	private List<Long> removedList;

	/**
	 * 初始化存储空间
	 */
	public PropInventory(int inventoryType, int gridCount, GamePlayer player) {
		this.player = player;
		this.inventoryType = inventoryType;
		aryProp = new Prop[gridCount];
		this.gridCount = gridCount;
		removedList = new ArrayList<Long>();
		changedPlaces = new ArrayList<Integer>();
	}

	public void init(Prop prop) {
		aryProp[prop.getPosIndex()] = prop;
		propCount += 1;
	}

	public int getInventoryType() {
		return inventoryType;
	}

	/**
	 * 获取指定格子中的道具
	 */
	public Prop getPropByPosIndex(int index) {
		if (index < 0 || index >= gridCount)
			return null;

		return aryProp[index];
	}

	/**
	 * 根据玩家道具Id获取对应的道具
	 */
	public Prop getPropByItemId(long itemId) {
		for (Prop prop : aryProp) {
			if (prop != null && prop.getPropInstance().getId() == itemId) {
				return prop;
			}
		}

		return null;
	}

	public int getSpecificPropCount(int propId) {
		int propCount = 0;
		for (int index = 0; index < gridCount; ++index) {
			Prop prop = aryProp[index];
			if (prop != null && prop.getPropTempId() == propId) {
				propCount += prop.getStackCount();
			}
		}

		return propCount;
	}

	/**
	 * 获取所有的道具
	 */
	public Prop[] getAllProp() {
		return aryProp;
	}

	/**
	 * 扩充背包
	 */
	public void expandPackageCount(int addCount) {
		gridCount += addCount;
		Prop[] newaryProp = new Prop[gridCount];
		for (int i = 0; i < aryProp.length; i++) {
			newaryProp[i] = aryProp[i];
		}
		aryProp = newaryProp;
	}

	/**
	 * 添加多少个道具到物品背包中，首先进行道具的叠加，把叠加剩余的道具添加到空的格子中
	 */
	public boolean addOnePropToPackage(Prop newProp, short addType) {
		// 优先进行道具数量的叠加操作
		boolean isSuccess = false;
		int leftCount = newProp.getStackCount();
		if (!newProp.getItemTempInfo().isEquip()) {
			for (Prop oldProp : aryProp) {
				if (oldProp != null && oldProp.getPropTempId() == newProp.getPropTempId() && oldProp.isCanStack()) {
					int orgStackCount = oldProp.getStackCount();
					int addCount = oldProp.getMaxStackCount() - orgStackCount;
					if (leftCount > 0 && addCount > 0) {
						int count = leftCount - addCount;
						if (count >= 0) {
							oldProp.setStackCount(oldProp.getMaxStackCount());
							leftCount -= addCount;
						} else {
							oldProp.setStackCount(orgStackCount + leftCount);
							leftCount = 0;
						}
						onPlaceChanged(oldProp.getPropInstance().getPosIndex());
						isSuccess = true;
					}

					// 无数量添加，跳出循环
					if (leftCount <= 0) {
						break;
					}
				}
			}
		}

		// 增加到空格子
		if (leftCount > 0) {
			int posIndex = getFirstFreeGrid();
			if (posIndex != Integer.MAX_VALUE) {
				newProp.setStackCount(leftCount);
				addPropByEmptyPos(newProp, posIndex);
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/**
	 * 指定一个空格子，将该物品添加到该格子
	 */
	public boolean addPropByEmptyPos(Prop prop, int posIndex) {
		prop.getPropInstance().setBagType(inventoryType);
		prop.setPosIndex(posIndex);
		aryProp[posIndex] = prop;
		onPlaceChanged(posIndex);
		propCount += 1;
		return true;
	}

	/**
	 * 改变指定索引位置的道具，这个操作并不是真正的删除道具，如果要删除道具请调用destroyPropByProp函数
	 */
	public boolean changePropByIndex(int posIndex) {
		if (posIndex >= gridCount || aryProp[posIndex] == null) {
			return false;
		}

		aryProp[posIndex] = null;
		onPlaceChanged(posIndex);
		propCount -= 1;
		return true;
	}

	/**
	 * 增加检测删除的道具数量是否大于自身堆叠数量，即增加count > prop.getStackCount()的条件，删除数量为count的某个道具
	 */
	public boolean destroyPropByProp(Prop prop, int count, short itemChangeType) {
		int orgStackCount = prop.getStackCount();
		int leftCount = orgStackCount - count;
		int posIndex = prop.getPosIndex();
		if (leftCount < 0) {
			Log.error("destroyPropByProp Error 背包拥有的数量为: " + orgStackCount + " 需要删除的数量为:  " + count + "。UserId为:  "
					+ player.getUserId() + ",道具删除类似为: " + itemChangeType);
			return false;
		} else if (leftCount == 0) {
			changePropByIndex(posIndex);
			synchronized (removedList) {
				removedList.add(prop.getPropInstance().getId()); // 添加到删除列表中
			}
		} else {
			prop.setStackCount(leftCount);
			onPlaceChanged(posIndex);
		}
		return true;
	}

	/**
	 * 获得第一个格子为空的索引
	 */
	public int getFirstFreeGrid() {
		for (int index = 0; index < gridCount; index++) {
			if (aryProp[index] == null) {
				return index;
			}
		}

		return Integer.MAX_VALUE;
	}

	/**
	 * 获取存储空间为空的格子数
	 */
	public int getFreeGridCount() {
		return gridCount - propCount;
	}

	public void onPlaceChanged(int place) {
		synchronized (lock) {
			if (!changedPlaces.contains(place)) {
				changedPlaces.add(place);
			}
		}

		if (changeCount.intValue() <= 0 && changedPlaces.size() > 0) {
			updateChangedProp();
		}
	}

	public void beginChanges() {
		changeCount.incrementAndGet();
	}

	public void commitChanges() {
		if (changeCount.decrementAndGet() <= 0 && changedPlaces.size() > 0) {
			updateChangedProp();
		}
	}

	/**
	 * 同步修改过的道具信息给客户端
	 */
	private void updateChangedProp() {
		List<Integer> tempChangedPlaces = new ArrayList<Integer>();
		synchronized (lock) {
			tempChangedPlaces.addAll(changedPlaces);
			changedPlaces.clear();
		}

		ItemChangeListMsg.Builder itemChangeList = ItemChangeListMsg.newBuilder();
		for (int posIndex : tempChangedPlaces) {
			Prop prop = aryProp[posIndex];
			ItemInfoMsg.Builder itemMsg = ItemInfoMsg.newBuilder();
			if (prop != null) {
				Prop.writePropInfo(prop.getPropInstance(), itemMsg);
			} else // 表示该位置的道具为空
			{
				itemMsg.setBagType(inventoryType);
				itemMsg.setPosIndex(posIndex);
				itemMsg.setStackCount(0);
			}
			itemChangeList.addItemChangeList(itemMsg);
		}

		// 发送到客户端
		player.sendPacket(Protocol.S_C_ITEM_CHANGE, itemChangeList);
	}

	/**
	 * 自动堆叠
	 */
	private void autoStackProp() {
		for (int i = 0; i < gridCount - 1; i++) {
			Prop prop = aryProp[i];
			if (prop != null && prop.isCanStack()) {
				Prop orgProp = null;
				for (int j = i + 1; j < gridCount; j++) {
					orgProp = aryProp[j];
					if (orgProp != null && orgProp.getPropTempId() == prop.getPropTempId()) {
						int orgStackCount = prop.getStackCount();
						int orgTempStackCount = orgProp.getStackCount();
						int totalCount = orgStackCount + orgTempStackCount;
						int maxStackCount = prop.getMaxStackCount();
						if (totalCount <= maxStackCount) {
							prop.setStackCount(totalCount);
							destroyPropByProp(orgProp, orgProp.getStackCount(), ItemChangeType.AUTO_STACK_COST);
						} else {
							prop.setStackCount(maxStackCount);
							int leftCount = totalCount - maxStackCount;
							orgProp.setStackCount(leftCount);
						}
						onPlaceChanged(prop.getPosIndex());
						onPlaceChanged(orgProp.getPosIndex());
					}
				}
			}
		}
	}

	/**
	 * 保证所有道具都是排在一起的，并没有存在中间存在为空的情况，为后面排序做优化，因为一旦到空的就可以直接break出去
	 */
	private void sortPropContinue() {
		int end = gridCount - 1;
		for (int i = 0; i < end; i++) {
			Prop prop = aryProp[i];
			if (prop == null) {
				for (int j = end; j > i; --j) { // 从后面往前找，找到第一个不为空的格子，然后进行交换
					Prop tempProp = aryProp[j];
					if (tempProp != null) {
						tempProp.setPosIndex(i);
						aryProp[i] = tempProp;
						aryProp[j] = null;
						end = j - 1;
						onPlaceChanged(i);
						onPlaceChanged(j);
						break;
					}
				}
			}
		}
	}

	private void exchangePropByIndex(Prop prop, int srcIndex, int destIndex) {
		Prop newProp = aryProp[destIndex];
		newProp.setPosIndex(srcIndex);
		aryProp[srcIndex] = newProp;
		prop.setPosIndex(destIndex);
		aryProp[destIndex] = prop;
		onPlaceChanged(srcIndex);
		onPlaceChanged(destIndex);
	}

	/**
	 * 自动整理背包
	 */
	public void autoPackUpSort() {
		autoStackProp();
		sortPropContinue();

		// 根据道具表的ItemOrder进行排序，ItemOrder由策划填写
		for (int i = 0; i < gridCount - 1; i++) {
			Prop prop = aryProp[i];
			if (prop == null) {
				break;
			}

			int index = i;
			int propOder = prop.getPropOrder();
			for (int j = i + 1; j < gridCount; j++) {
				Prop tempProp = aryProp[j];
				if (tempProp == null) {
					break;
				}

				if (tempProp.getPropOrder() < propOder) {
					propOder = tempProp.getPropOrder();
					index = j;
				}
			}

			if (index != i) {
				exchangePropByIndex(prop, i, index);
			}
		}
	}

	/**
	 * 根据道具模板id 移除指定数量的道具
	 */
	public boolean removePropByPropId(int propId, int count, short itemChangeType) {
		int itemCount = count;
		List<Integer> changedAllPos = new ArrayList<Integer>();
		for (int i = 0; i < gridCount; i++) {
			Prop prop = aryProp[i];
			if (prop != null && prop.getPropTempId() == propId) {
				itemCount = itemCount - prop.getPropInstance().getStackCount();
				changedAllPos.add(i);
				if (itemCount <= 0) {
					break;
				}
			}
		}

		// 删除已查好位置的物品
		if (itemCount <= 0) {
			itemCount = count;
			try {
				beginChanges();
				for (int i : changedAllPos) {
					Prop prop = aryProp[i];
					if (prop != null && prop.getPropTempId() == propId) {
						if (prop.getPropInstance().getStackCount() <= itemCount) {
							int removerCount = prop.getPropInstance().getStackCount();
							if (destroyPropByProp(prop, removerCount, itemChangeType)) {
								itemCount = itemCount - removerCount;
							}
						} else {
							if (destroyPropByProp(prop, itemCount, itemChangeType)) {
								itemCount = 0;
							}
						}
					}
				}

				if (itemCount != 0) {
					Log.error("Remove template error:last item cout not equal Zero.userId : " + player.getUserId()
							+ " , propId : " + propId + ", count : " + count);
				}
			} catch (Exception e) {
				Log.error("removeTemplate error. userId : " + player.getUserId() + " , propId : " + propId
						+ ", count : " + count, e);
			} finally {
				commitChanges();
			}
			return true;
		}

		return false;
	}

	/**
	 * 将背包的数据保存到数据库中
	 */
	public void saveToDB(long userId) {
		List<Prop> saveList = new ArrayList<Prop>();
		List<Long> tempRemovedList = new ArrayList<Long>();
		synchronized (lock) {
			for (Prop item : aryProp) {
				if (item != null) {
					saveList.add(item);
				}
			}

			if (removedList.size() > 0) {
				tempRemovedList.addAll(removedList);
				removedList.clear();
			}
		}

		if (saveList.size() > 0) {
			for (Prop info : saveList) {
				PropInstance itemInfo = info.getPropInstance();
				if (itemInfo.getOp() == DBOption.INSERT) {
					DaoMgr.itemInfoDao.addPropInstance(userId, itemInfo);
				}

				if (itemInfo.getOp() == DBOption.UPDATE) {
					DaoMgr.itemInfoDao.updateItemInfo(userId, itemInfo);
				}
			}
		}

		if (tempRemovedList.size() > 0) {
			for (long itemId : tempRemovedList) {
				DaoMgr.itemInfoDao.deleteItemInfo(itemId);
			}
		}
	}
}
