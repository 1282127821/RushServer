package com.star.light.socket.cmd.prop;

import com.star.light.player.GamePlayer;
import com.star.light.player.ItemChangeType;
import com.star.light.prop.BagType;
import com.star.light.prop.HoleState;
import com.star.light.prop.PlayerPropMgr;
import com.star.light.prop.Prop;
import com.star.light.prop.PropInventory;
import com.star.light.prop.PropType;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.table.ConfigMgr;
import com.star.light.table.EquipForbiddenInfoMgr;
import com.star.light.table.EquipInlayInfo;
import com.star.light.table.EquipInlayInfoMgr;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.EquipInlayMsg;

public class EquipInlayCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		EquipInlayMsg netMsg = EquipInlayMsg.parseFrom(packet.getMsgBody());
		int operType = netMsg.getOperType();
		int inlayPosIndex =  netMsg.getInlayPosIndex();
		if (inlayPosIndex < HoleState.HOLE_ONE || inlayPosIndex > HoleState.HOLE_THREE) {
			return;
		}
		
		if (operType == 1) {
			equipInlay(player, operType, netMsg.getBagType(), netMsg.getPosIndex(), inlayPosIndex, netMsg.getCardPosIndex());
		} else if(operType == 2) {
			equipUnInlay(player, operType, netMsg.getBagType(), netMsg.getPosIndex(), inlayPosIndex);
		}
	}
	
	/**
	 * 装备镶嵌
	 */
	private  void equipInlay(GamePlayer player, int operType, int bagType, int posIndex, int inlayPosIndex, int cardPosIndex) {
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("装备镶嵌卡片错误, PlayerPropMgr为空." + ", UserId: " + player.getUserId());
			return;
		}

		PropInventory equipInventory = propMgr.getPropInventory(bagType);
		if (equipInventory == null) {
			GameLog.error("装备镶嵌卡片错误, equipInventory为空." + ", UserId: " + player.getUserId());
			return;
		}

		PropInventory propInventory = propMgr.getPropInventory(BagType.PACKAGE);
		if (propInventory == null) {
			GameLog.error("装备镶嵌卡片错误, propInventory为空." + ", UserId: " + player.getUserId());
			return;
		}

		Prop equip = equipInventory.getPropByPosIndex(posIndex);
		if (equip == null) {
			GameLog.error("装备镶嵌卡片错误, 装备为空，位置为:  " + posIndex + ", UserId: " + player.getUserId());
			return;
		}

		int masterType = equip.getMasterType();
		if (equip.getMasterType() != PropType.EQUIP) {
			GameLog.error("装备镶嵌卡片出错, 此道具不是装备 MasterType:  " + masterType +  ", UserId: " + player.getUserId());
			return;
		}
		
		Prop cardMaterialInfo = propInventory.getPropByPosIndex(cardPosIndex);
		if (cardMaterialInfo == null) {
			GameLog.error("装备镶嵌卡片错误, 镶嵌的卡片为空，位置为:  " + cardPosIndex + ", UserId: " + player.getUserId());
			return;
		}

		int equipId = equip.getItemTempInfo().itemId;
		boolean isForbidden = EquipForbiddenInfoMgr.getInstance().isEquipInlayForbidden(equipId);
		if (isForbidden) {
			player.sendTips(1038);
			GameLog.error("装备镶嵌卡片错误, 该装备不能够被镶嵌，equipId为:  " + equipId + ", UserId: " + player.getUserId());
			return;
		}

		int cardId = cardMaterialInfo.getItemTempInfo().itemId;
		EquipInlayInfo equipInlayInfo = EquipInlayInfoMgr.getInstance().getInlayCardInfo(cardId);
		if (equipInlayInfo == null) {
			GameLog.error("装备镶嵌卡片错误, 镶嵌的卡片不对，在表中找不到，cardId为:  " + cardId + ", UserId: " + player.getUserId());
			return;
		}

		boolean isRightPos = false;
		int[] aryInlayPos = equipInlayInfo.inlayPos;
		for (int pos : aryInlayPos) {
			if (pos == equip.getSonType()) {
				isRightPos = true;
				break;
			}
		}

		if (!isRightPos) {
			return;
		}

		if (equip.isInlayCard(inlayPosIndex)) {
			GameLog.error("装备镶嵌卡片错误, 当前槽位已有卡片，需要先摘取才可以镶嵌:  " + inlayPosIndex + ", UserId: " + player.getUserId());
			return;
		}

		boolean isCanInlay = false;
		int playerLv = player.getPlayerLv();
		if (inlayPosIndex == HoleState.HOLE_ONE && playerLv >= ConfigMgr.equipInlayOneLv) {
			isCanInlay = true;
		} else if (inlayPosIndex == HoleState.HOLE_TWO && playerLv >= ConfigMgr.equipInlayTwoLv) {
			isCanInlay = true;
		} else if (inlayPosIndex == HoleState.HOLE_THREE && playerLv >= ConfigMgr.equipInlayThreeLv) {
			isCanInlay = true;
		}

		if (!isCanInlay) {
			GameLog.error("装备镶嵌卡片错误, 玩家等级: " + playerLv + ",镶嵌的孔位为: " + inlayPosIndex + ", UserId: " + player.getUserId());
			return;
		}
		
		int costGold = ConfigMgr.equipInlayBase + cardMaterialInfo.getQuality() * ConfigMgr.equipInlayQualityFactor;
		int haveGold = player.getGold();
		if (costGold > haveGold) {
			GameLog.error("装备镶嵌卡片错误, 所需金钱: " + costGold + ",当前金钱: " + haveGold + ", UserId: " + player.getUserId());
			return;
		}
		
		if(propMgr.destroyPropByProp(BagType.PACKAGE, cardMaterialInfo, 1, ItemChangeType.EQUIP_INLAY_COST) && player.removeGold(costGold, ItemChangeType.EQUIP_INLAY_COST)) {
			equip.setInlayCardId(inlayPosIndex, cardId);
			if (bagType == BagType.EQUIP_FENCE) {
				player.addEquipAttribute(posIndex, true);
			}
			equipInventory.onPlaceChanged(posIndex);
			propInventory.onPlaceChanged(cardPosIndex);

			EquipInlayMsg.Builder netMsg = EquipInlayMsg.newBuilder();
			netMsg.setOperType(operType);
			netMsg.setBagType(bagType);
			netMsg.setPosIndex(posIndex);
			netMsg.setInlayPosIndex(inlayPosIndex);
			player.sendPacket(Protocol.S_C_EQUIP_INLAY, netMsg);
		}
	}

	/**
	 * 摘取卡片
	 */
	private void equipUnInlay(GamePlayer player, int operType, int bagType, int posIndex, int inlayPosIndex) {
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("equipUnInlay Error, PlayerPropMgr is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		PropInventory equipInventory = propMgr.getPropInventory(bagType);
		if (equipInventory == null) {
			GameLog.error("equipUnInlay Error, equipInventory is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		Prop equip = equipInventory.getPropByPosIndex(posIndex);
		if (equip == null) {
			GameLog.error("equipUnInlay Error, 道具为空，位置为:  " + posIndex + ", UserId: " + player.getUserId());
			return;
		}

		int masterType = equip.getMasterType();
		if (equip.getMasterType() != PropType.EQUIP) {
			GameLog.error("装备摘取卡片出错, 此道具不是装备 MasterType:  " + masterType +  ", UserId: " + player.getUserId());
			return;
		}
		
		if (!equip.isInlayCard(inlayPosIndex)) {
			GameLog.error("equipUnInlay Error, 当前槽位并没有卡片可以摘取:  " + inlayPosIndex + ", UserId: " + player.getUserId());
			return;
		}

		equip.setInlayCardId(inlayPosIndex, 0);
		if (bagType == BagType.EQUIP_FENCE) {
			player.addEquipAttribute(posIndex, true);
		}
		equipInventory.onPlaceChanged(posIndex);

		EquipInlayMsg.Builder netMsg = EquipInlayMsg.newBuilder();
		netMsg.setOperType(operType);
		netMsg.setBagType(bagType);
		netMsg.setPosIndex(posIndex);
		netMsg.setInlayPosIndex(inlayPosIndex);
		player.sendPacket(Protocol.S_C_EQUIP_INLAY, netMsg);
	}
}