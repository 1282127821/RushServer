package com.star.light.socket.cmd.prop;

import java.util.concurrent.ThreadLocalRandom;

import com.star.light.player.FightAttributeType;
import com.star.light.player.GameConst;
import com.star.light.player.GamePlayer;
import com.star.light.player.ItemChangeType;
import com.star.light.player.PropertyInfo;
import com.star.light.prop.BagType;
import com.star.light.prop.EquipBlessResult;
import com.star.light.prop.PlayerPropMgr;
import com.star.light.prop.Prop;
import com.star.light.prop.PropInventory;
import com.star.light.prop.PropType;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.table.ConfigMgr;
import com.star.light.table.EquipBlessEffectInfo;
import com.star.light.table.EquipBlessEffectInfoMgr;
import com.star.light.table.EquipBlessInfo;
import com.star.light.table.EquipBlessInfoMgr;
import com.star.light.table.EquipForbiddenInfoMgr;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.EquipBlessMsg;

public class EquipBlessCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		EquipBlessMsg netMsg = EquipBlessMsg.parseFrom(packet.getMsgBody());
		int bagType = netMsg.getBagType();
		int posIndex = netMsg.getPosIndex();
		int blessMaterialId = netMsg.getBlessMaterialId();
		PlayerPropMgr propMgr = player.getPropMgr();
		if (propMgr == null) {
			GameLog.error("装备祝福出错, PlayerPropMgr is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		PropInventory equipInventory = propMgr.getPropInventory(bagType);
		if (equipInventory == null) {
			GameLog.error("装备祝福出错, equipInventory is NULL." + ", UserId: " + player.getUserId());
			return;
		}

		PropInventory propInventory = propMgr.getPropInventory(BagType.PACKAGE);
		if (propInventory == null) {
			GameLog.error("装备祝福出错, propInventory为空." + ", UserId: " + player.getUserId());
			return;
		}
		
		Prop equip = equipInventory.getPropByPosIndex(posIndex);
		if (equip == null) {
			GameLog.error("装备祝福出错, 道具为空，位置为:  " + posIndex +  ", UserId: " + player.getUserId());
			return;
		}
		
		int masterType = equip.getMasterType();
		if (equip.getMasterType() != PropType.EQUIP) {
			GameLog.error("装备祝福出错, 此道具不是装备 MasterType:  " + masterType +  ", UserId: " + player.getUserId());
			return;
		}

		int equipId = equip.getItemTempInfo().itemId;
		boolean isForbidden = EquipForbiddenInfoMgr.getInstance().isEquipBlessForbidden(equipId);
		if (isForbidden) {
			player.sendTips(1036);
			GameLog.error("装备祝福出错, 该装备不能够被祝福，equipId为:  " + equipId + ", UserId: " + player.getUserId());
			return;
		}
		
		int srcBlessLv = equip.getBlessLv();
		if (srcBlessLv >= ConfigMgr.equipBlessMaxLv) {
			player.sendTips(1034);
			GameLog.error("装备祝福出错, 找不到对应的祝福材料，装备当前祝福等级: " + srcBlessLv + ", UserId: " + player.getUserId());
			return;
		}
		
		int equipQuality = equip.getQuality();
		EquipBlessInfo equipBlessInfo = EquipBlessInfoMgr.getInstance().getEquipBlessInfo(equipQuality, srcBlessLv);
		if (equipBlessInfo == null) {
			GameLog.error("装备祝福出错, 找不到对应的祝福材料，装备品级: " + equipQuality + ",装备当前祝福等级: " + srcBlessLv + ", UserId: " + player.getUserId());
			return;
		}
		
		EquipBlessEffectInfo equipBlessEffectInfo = EquipBlessEffectInfoMgr.getInstance().getEquipBlessEffectInfo(equipQuality);
		if (equipBlessEffectInfo == null) {
			GameLog.error("装备祝福出错, 找不到对应的装备效果，装备品级: " + equipQuality + ", UserId: " + player.getUserId());
			return;
		}
	
		int equipLv = equip.getItemTempInfo().itemLv;
		double levelFactor = Math.pow(equipLv, ConfigMgr.equipBlessLevelFactor);
		double qualityFactor = (1 + equipQuality / ConfigMgr.equipBlessQualityFactor);
		int costGold = (int)(Math.ceil((ConfigMgr.equipBlessBase * 1.0 + levelFactor) * qualityFactor));
		int haveGold = player.getGold();
		if (costGold > haveGold) {
			GameLog.error("装备祝福出错, 所需金钱: " + costGold + ",当前金钱: " + haveGold + ", UserId: " + player.getUserId());
			return;
		}
		
		int needCount = 0;		
		if (blessMaterialId == equipBlessInfo.material1Id) {
			needCount = equipBlessInfo.material1Num;
		} else if (blessMaterialId == equipBlessInfo.material2Id) {
			needCount = equipBlessInfo.material2Num;
		}
			
		if (needCount == 0) {
			GameLog.error("装备祝福出错, 使用的祝福材料不对 blessMaterialId: " + blessMaterialId + ", UserId: " + player.getUserId());
			return;
		}
		
		int haveCount = propInventory.getSpecificPropCount(blessMaterialId);
		if (needCount > haveCount) {
			GameLog.error("装备祝福出错, 材料数量不对 所需材料数量: " + needCount + ",当前拥有材料数量: " + haveCount + ", UserId: " + player.getUserId());
			return;
		}
		
		if (propMgr.removePropByPropId(blessMaterialId, needCount, ItemChangeType.EQUIP_BLESS_COST) && player.removeGold(costGold, ItemChangeType.EQUIP_BLESS_COST)) {
			int newBlessLv = srcBlessLv;
			int blessResult = EquipBlessResult.BLESS_INVALID;
			if (srcBlessLv <= equipBlessEffectInfo.safeLv) {
				newBlessLv += 1;
				blessResult = EquipBlessResult.BLESS_SUCCESS;
			} else {
				int lvChanceFactor = (int)Math.floor(100 - 3 * Math.pow((Math.floorDiv(equipLv, 10) + 1), ConfigMgr.equipBlessMaterialLevelFactor));
				GameLog.info("LZGLZG  lvChanceFactor " + lvChanceFactor);
				int qualityChanceFactor = (int)Math.floor(100 - 4 * Math.pow(equipQuality, ConfigMgr.equipBlessMaterialQualityFactor));
				GameLog.info("LZGLZG  qualityChanceFactor  " + qualityChanceFactor);
				int blessLvChanceFactor = (int)Math.floor(100 - Math.pow((2 + 2 * srcBlessLv), ConfigMgr.equipBlessLvFactor));
				GameLog.info("LZGLZG  blessLvChanceFactor  " + blessLvChanceFactor);
				int successChance  = (int)Math.floorDiv(lvChanceFactor * qualityChanceFactor * blessLvChanceFactor, 100);
				GameLog.info("LZGLZG  successChance   " + successChance);
				if (blessMaterialId == equipBlessInfo.material1Id) {
					double otherChance = 0.01 + successChance * GameConst.TEN_THOUSAND_RATE * 1.05;
					GameLog.info("LZGLZG  otherChance   " + otherChance);
					successChance = (int)Math.floor(otherChance * GameConst.TEN_THOUSAND);
					GameLog.info("LZGLZG  successChance  II " + successChance);
				}
				
				int blessChance = ThreadLocalRandom.current().nextInt(1, GameConst.TEN_THOUSAND + 1);
				int invalidChance = (int)Math.floor((1 - successChance * GameConst.TEN_THOUSAND_RATE) * 0.82 * GameConst.TEN_THOUSAND);
				if (blessChance <= successChance) {
					blessResult = EquipBlessResult.BLESS_SUCCESS;
					newBlessLv += 1;
				} else if (blessChance > successChance && blessChance <= successChance + invalidChance) {
					blessResult = EquipBlessResult.BLESS_INVALID;
				} else if (blessChance > successChance + invalidChance && blessChance <= GameConst.TEN_THOUSAND) {
					blessResult = EquipBlessResult.BLESS_FAIL;
					newBlessLv -= 1;
				}
			}
			
			if (blessResult == EquipBlessResult.BLESS_SUCCESS) {
				PropertyInfo propertyInfo = equip.getItemTempInfo().aryAttributeValue[0];
				int blessAttribute = equip.getBlessAttribute();
				if (propertyInfo.type == FightAttributeType.ATTACK) {
					blessAttribute += equipBlessEffectInfo.fixedAttack;
					if (newBlessLv > equipBlessEffectInfo.safeLv) {
						blessAttribute += ThreadLocalRandom.current().nextInt(equipBlessEffectInfo.minDangerAttack, equipBlessEffectInfo.maxDangerAttack + 1);
					}
					
					if (newBlessLv >= ConfigMgr.equipBlessOverLv) {
						blessAttribute += equipBlessEffectInfo.addAttack;
					}
				} else if (propertyInfo.type == FightAttributeType.DEFENCE) {
					blessAttribute += equipBlessEffectInfo.fixedDefence;
					if (newBlessLv > equipBlessEffectInfo.safeLv) {
						blessAttribute += ThreadLocalRandom.current().nextInt(equipBlessEffectInfo.minDangerDefence, equipBlessEffectInfo.maxDangerDefence + 1);
					}
					
					if (newBlessLv >= ConfigMgr.equipBlessOverLv) {
						blessAttribute += equipBlessEffectInfo.addDefence;
					}
				}
				equip.setBlessAttribute(blessAttribute);
			} else if (blessResult == EquipBlessResult.BLESS_FAIL) {
				PropertyInfo propertyInfo = equip.getItemTempInfo().aryAttributeValue[0];
				int blessAttribute = equip.getBlessAttribute();
				if (propertyInfo.type == FightAttributeType.ATTACK) {
					blessAttribute -= equipBlessEffectInfo.fixedAttack;
					if (srcBlessLv > equipBlessEffectInfo.safeLv) {
						blessAttribute -= ThreadLocalRandom.current().nextInt(equipBlessEffectInfo.minDangerAttack, equipBlessEffectInfo.maxDangerAttack + 1);
					}
					
					if (srcBlessLv >= ConfigMgr.equipBlessOverLv) {
						blessAttribute -= equipBlessEffectInfo.addAttack;
					}
				} else if (propertyInfo.type == FightAttributeType.DEFENCE) {
					blessAttribute -= equipBlessEffectInfo.fixedDefence;
					if (srcBlessLv > equipBlessEffectInfo.safeLv) {
						blessAttribute -= ThreadLocalRandom.current().nextInt(equipBlessEffectInfo.minDangerDefence, equipBlessEffectInfo.maxDangerDefence + 1);
					}
					
					if (srcBlessLv >= ConfigMgr.equipBlessOverLv) {
						blessAttribute -= equipBlessEffectInfo.addDefence;
					}
				}
				equip.setBlessAttribute(blessAttribute);
			}
			
			equip.setBlessLv(newBlessLv);
			if (bagType == BagType.EQUIP_FENCE) {
				player.addEquipAttribute(posIndex, true); 
			}
			equipInventory.onPlaceChanged(posIndex);
			EquipBlessMsg.Builder blessMsg = EquipBlessMsg.newBuilder();
			blessMsg.setBagType(bagType);
			blessMsg.setPosIndex(posIndex);
			blessMsg.setBlessResult(blessResult);
			player.sendPacket(Protocol.S_C_EQUIP_BLESS, blessMsg);
		}
	}
}