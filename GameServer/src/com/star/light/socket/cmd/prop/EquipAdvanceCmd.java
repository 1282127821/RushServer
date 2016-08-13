package com.star.light.socket.cmd.prop;

import java.util.concurrent.ThreadLocalRandom;

import com.star.light.player.GameConst;
import com.star.light.player.GamePlayer;
import com.star.light.player.ItemChangeType;
import com.star.light.prop.BagType;
import com.star.light.prop.EquipAdvanceResult;
import com.star.light.prop.PlayerPropMgr;
import com.star.light.prop.Prop;
import com.star.light.prop.PropInventory;
import com.star.light.prop.PropTemplate;
import com.star.light.prop.PropType;
import com.star.light.prop.QualityType;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.table.ConfigMgr;
import com.star.light.table.EquipAdvanceInfo;
import com.star.light.table.EquipAdvanceInfoMgr;
import com.star.light.table.EquipForbiddenInfoMgr;
import com.star.light.table.ItemTemplateMgr;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.EquipAdvanceMsg;

public class EquipAdvanceCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		EquipAdvanceMsg netMsg = EquipAdvanceMsg.parseFrom(packet.getMsgBody());
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("装备进阶出错, PlayerPropMgr is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		int bagType = netMsg.getBagType();
		PropInventory equipInventory = propMgr.getPropInventory(bagType);
		if (equipInventory == null) {
			GameLog.error("装备进阶出错, equipInventory is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		int posIndex = netMsg.getPosIndex();
		Prop equip = equipInventory.getPropByPosIndex(posIndex);
		if (equip == null) {
			GameLog.error("装备进阶出错, 道具为空，位置为:  " + posIndex + ", UserId: " + player.getUserId());
			return;
		}

		int masterType = equip.getMasterType();
		if (equip.getMasterType() != PropType.EQUIP) {
			GameLog.error("装备进阶出错, 此道具不是装备 MasterType:  " + masterType +  ", UserId: " + player.getUserId());
			return;
		}
		
		int equipId = equip.getItemTempInfo().itemId;
		boolean isForbidden = EquipForbiddenInfoMgr.getInstance().isEquipAdvanceForbidden(equipId);
		if (isForbidden) {
			player.sendTips(1037);
			GameLog.error("装备进阶出错, 该装备无法被进阶, EquipId: " + equipId + ", UserId: " + player.getUserId());
			return;
		}

		int quality = equip.getQuality();
		if (quality >= QualityType.COUNT) {
			player.sendTips(1035);
			GameLog.error("装备进阶出错, 装备品质已经为最高, Quality:  " + quality + ", UserId: " + player.getUserId());
			return;
		}

		int newQuality = quality + 1;
		int sonType = equip.getSonType();
		PropTemplate propTemplate = ItemTemplateMgr.getInstance().getEquipAdvanceProp(sonType, newQuality);
		if (propTemplate == null) {
			GameLog.error("装备进阶出错, 装备品质已经为最高, Quality:  " + newQuality + ", 装备子类型为 SonType: " + sonType +  ", UserId: " + player.getUserId());
			return;
		}
		
		EquipAdvanceInfo equipAdvanceInfo = EquipAdvanceInfoMgr.getInstance().getEquipAdvanceInfo(quality);
		if (equipAdvanceInfo == null) {
			GameLog.error("装备进阶出错, 装备品质已经为最高, Quality:  " + quality + ", UserId: " + player.getUserId());
			return;
		}
		
		int costGold = (ConfigMgr.equipAdvanceBase + equip.getItemTempInfo().itemLv * ConfigMgr.equipAdvanceLevelFactor) + quality * ConfigMgr.equipAdvanceQualityFactor;
		int haveGold = player.getGold();
		if (costGold > haveGold) {
			GameLog.error("装备进阶出错, 所需金钱: " + costGold + ",当前金钱: " + haveGold + ", UserId: " + player.getUserId());
			return;
		}
		
		int lowMaterialId = netMsg.getLowMaterialId();
		int lowMaterialNum = netMsg.getLowMaterialNum();
		int highMaterialId = netMsg.getHighMaterialId();
		int highMaterialNum = netMsg.getHighMaterialNum();
		if (lowMaterialNum == 0 && highMaterialNum == 0) {
			GameLog.error("装备进阶出错, lowMaterialNum: " + lowMaterialNum + ",highMaterialNum: " + highMaterialNum + ", UserId: " + player.getUserId());
		}
		
		double equipAdvanceChance = 0.0;
		if (lowMaterialId == equipAdvanceInfo.lowMaterialId && lowMaterialNum <= GameConst.MAX_EQUIP_ADVANCE_MATERIAL_COUNT) {
			if (propMgr.removePropByPropId(lowMaterialId, lowMaterialNum, ItemChangeType.EQUIP_ADVANCE_COST)) {
				for (int i = 1; i <= lowMaterialNum; i++) {
					equipAdvanceChance += equipAdvanceInfo.lowMaterialChance * (110 - ConfigMgr.equipAdvanceReducePercent * i) / 100.0;
				}
			}
		}
		
		if (highMaterialId == equipAdvanceInfo.highMaterialId && highMaterialNum <= GameConst.MAX_EQUIP_ADVANCE_MATERIAL_COUNT) {
			if (propMgr.removePropByPropId(highMaterialId, highMaterialNum, ItemChangeType.EQUIP_ADVANCE_COST)) {
				for (int i = 1; i <= highMaterialNum; i++) {
					equipAdvanceChance += equipAdvanceInfo.highMaterialChance * (110 - ConfigMgr.equipAdvanceReducePercent * i) / 100.0;
				}
			}
		}
		
		if (player.removeGold(costGold, ItemChangeType.EQUIP_ADVANCE_COST)) {
			int advanceChance = (int)Math.ceil(equipAdvanceChance * GameConst.HUNDRED_PERCENT);
			int randomChance = ThreadLocalRandom.current().nextInt(0, GameConst.TEN_THOUSAND);
			int advanceResult = EquipAdvanceResult.ADVANCE_FAIL;
			if (randomChance < advanceChance) {
				equip.setItemTempInfo(propTemplate);
				equip.getPropInstance().setTemplateId(propTemplate.itemId);
				if (bagType == BagType.EQUIP_FENCE) {
					player.addEquipAttribute(posIndex, true);
				}
				equipInventory.onPlaceChanged(posIndex);
				advanceResult = EquipAdvanceResult.ADVANCE_SUCCESS;
			}
			
			EquipAdvanceMsg.Builder advanceMsg = EquipAdvanceMsg.newBuilder();
			advanceMsg.setBagType(bagType);
			advanceMsg.setPosIndex(posIndex);
			advanceMsg.setAdvanceResult(advanceResult);
			player.sendPacket(Protocol.S_C_EQUIP_ADVANCE, advanceMsg);
		}
	}
}