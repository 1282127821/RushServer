package com.star.light.socket.cmd.prop;

import com.star.light.player.GamePlayer;
import com.star.light.player.ItemChangeType;
import com.star.light.prop.BagType;
import com.star.light.prop.PlayerPropMgr;
import com.star.light.prop.Prop;
import com.star.light.prop.PropInventory;
import com.star.light.prop.PropType;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.PropOperatorMsg;

public class PropOperatorCmd implements NetCmd {
	/**
	 * 1装备穿上 2装备卸下  3使用道具 4道具出售
	 */
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		PropOperatorMsg msg = PropOperatorMsg.parseFrom(packet.getMsgBody());
		int operatorType = msg.getOperatorType();
		int posIndex = msg.getSrcPosIndex();
		switch (operatorType) {
		case 1:
			putOnEquip(player, posIndex);
			break;

		case 2:
			unEquip(player, posIndex);
			break;
			
		case 3:
			sellProp(player, msg.getSrcBagType(), posIndex, msg.getPropCount());
			break;

		case 4:
			player.getPropMgr().autoPackUpSort();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 穿上装备
	 */
	private void putOnEquip(GamePlayer player, int posIndex) {
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("穿装备出错, PlayerPropInventory is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		Prop equip = propMgr.getPropByPosIndex(BagType.PACKAGE, posIndex);
		if (equip == null) {
			return;
		}
		
		int masterType = equip.getMasterType();
		if (equip.getMasterType() != PropType.EQUIP) {
			GameLog.error("穿装备出错, 此道具不是装备 MasterType:  " + masterType +  ", UserId: " + player.getUserId());
			return;
		}
		
		PropInventory packageInventory = propMgr.getPropInventory(BagType.PACKAGE);
		PropInventory equipInventory = propMgr.getPropInventory(BagType.EQUIP_FENCE);
		equipInventory.beginChanges();
		packageInventory.beginChanges();
		int equipIndex = equip.getEquipIndex();
		boolean isSuccess = propMgr.exchangeEquip(BagType.PACKAGE, posIndex, BagType.EQUIP_FENCE, equipIndex);
		if (isSuccess) {
			player.addEquipAttribute(equipIndex, true);
		}

		equipInventory.commitChanges();
		packageInventory.commitChanges();
	}

	/**
	 * 卸载装备
	 */
	private void unEquip(GamePlayer player, int posIndex) {
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("unEquip Error, PlayerPropInventory is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		Prop equip = propMgr.getPropByPosIndex(BagType.EQUIP_FENCE, posIndex);
		if (equip == null) {
			return;
		}

		int masterType = equip.getMasterType();
		if (equip.getMasterType() != PropType.EQUIP) {
			GameLog.error("卸载装备出错, 此道具不是装备 MasterType:  " + masterType +  ", UserId: " + player.getUserId());
			return;
		}
		
		int destIndex = propMgr.getFirstFreeGrid(BagType.PACKAGE);
		if (destIndex == Integer.MAX_VALUE) {
			player.sendTips(1013);
			return;
		}

		PropInventory packageInventory = propMgr.getPropInventory(BagType.PACKAGE);
		PropInventory equipInventory = propMgr.getPropInventory(BagType.EQUIP_FENCE);
		equipInventory.beginChanges();
		packageInventory.beginChanges();
		boolean isSuccess = propMgr.exchangeEquip(BagType.EQUIP_FENCE, posIndex, BagType.PACKAGE, destIndex);
		if (isSuccess) {
			player.addEquipAttribute(posIndex, true);
		}

		equipInventory.commitChanges();
		packageInventory.commitChanges();
	}

	/**
	 * 玩家出售道具
	 */
	private void sellProp(GamePlayer player, int inventoryType, int posIndex, int propCount) {
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("sellProp Error, PlayerPropInventory is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		Prop prop = propMgr.getPropByPosIndex(inventoryType, posIndex);
		if (prop == null) {
			GameLog.error("sellProp Error, 道具为空，位置为:  " + posIndex + ", UserId: " + player.getUserId());
			return;
		}

		if (propCount <= 0 || prop.getStackCount() < propCount) {
			GameLog.error("sellProp Error, SellCount: " + propCount + ", propStackCount: " + prop.getStackCount()
					+ ", UserId: " + player.getUserId());
			return;
		}

		boolean isSuccess = propMgr.destroyPropByProp(inventoryType, prop, propCount, ItemChangeType.SELL_PROP);
		if (isSuccess) {
			player.addGold(prop.getItemTempInfo().sellGold * propCount, ItemChangeType.SELL_PROP);
		}
	}
}
