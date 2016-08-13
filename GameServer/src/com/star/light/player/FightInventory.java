package com.star.light.player;

import com.star.light.protocol.Protocol;

import tbgame.pbmessage.GamePBMsg.FightAttributeMsg;

public class FightInventory {
	private GamePlayer player;
	/**
	 * 玩家所有属性的加成
	 */
	private Property[] properties;

	public FightInventory(GamePlayer player) {
		this.player = player;
		properties = new Property[FightAttributeType.COUNT]; // 属性数组
		for (int index = FightAttributeType.STRENGTH; index < FightAttributeType.COUNT; index++) {
			properties[index] = new Property();
		}
	}

	public int getTotalProperty(int property) {
		return properties[property].getTotalJoin();
	}

	public FightAttributeMsg.Builder writeFightAttributeMsg() {
		FightAttributeMsg.Builder fightAttributeMsg = FightAttributeMsg.newBuilder();
		fightAttributeMsg.setStrength(properties[FightAttributeType.STRENGTH].getTotalJoin());
		fightAttributeMsg.setAgility(properties[FightAttributeType.AGILITY].getTotalJoin());
		fightAttributeMsg.setVitality(properties[FightAttributeType.VITALITY].getTotalJoin());
		fightAttributeMsg.setIntelligence(properties[FightAttributeType.INTELLIGENCE].getTotalJoin());
		fightAttributeMsg.setDexterity(properties[FightAttributeType.DEXTERITY].getTotalJoin());
		fightAttributeMsg.setLucky(properties[FightAttributeType.LUCKY].getTotalJoin());
		fightAttributeMsg.setAttack(properties[FightAttributeType.ATTACK].getTotalJoin());
		fightAttributeMsg.setDefence(properties[FightAttributeType.DEFENCE].getTotalJoin());
		fightAttributeMsg.setMagicattack(properties[FightAttributeType.MAGIC_ATTACK].getTotalJoin());
		fightAttributeMsg.setMagicdefence(properties[FightAttributeType.MAGIC_DEFENCE].getTotalJoin());
		fightAttributeMsg.setAttackspeed(properties[FightAttributeType.ATTACK_SPEED].getTotalJoin());
		fightAttributeMsg.setMagiccold(properties[FightAttributeType.MAGIC_COLD].getTotalJoin());
		fightAttributeMsg.setHitting(properties[FightAttributeType.HIT].getTotalJoin());
		fightAttributeMsg.setDodge(properties[FightAttributeType.DODGE].getTotalJoin());
		return fightAttributeMsg;
	}

	/**
	 * 计算战斗力
	 */
	public int calcFight() {
		int totalFight = 0;
		for (int livingIndex = FightAttributeType.STRENGTH; livingIndex <= FightAttributeType.DODGE; livingIndex++) {
			totalFight += getTotalProperty(livingIndex);
		}

		return totalFight;
	}
	
	public void addCharacterHP(int charHP) {
		properties[FightAttributeType.HP].setCharacterHP(charHP);
	}
	
	/**
	 * 增加玩家等级的属性加成
	 */
	public void addLevelAttribute(int[] levelUpData) {
		for (int livingIndex = FightAttributeType.STRENGTH; livingIndex < FightAttributeType.COUNT; livingIndex++) {
			properties[livingIndex].setLevelData(levelUpData[livingIndex]);
		}
	}

	/**
	 * 增加玩家装备的属性加成
	 */
	public void addEquipAttribute(int index, int[] equipAttribute) {
		for (int livingIndex = FightAttributeType.STRENGTH; livingIndex < FightAttributeType.COUNT; livingIndex++) {
			properties[livingIndex].addEquipData(index, equipAttribute[livingIndex]);
		}
	}

	/**
	 * 清空玩家的装备属性加成
	 */
	public void clearEquipAttribute(int index) {
		for (int i = 0; i < properties.length; ++i) {
			properties[i].clearEquipData(index);
		}
	}

	/**
	 * 卸载内存数据
	 */
	public void unloadData() {
		properties = null;
	}

	/**
	 * 同步玩家的战斗属性给客户端
	 */
	public void syncFightAttribute() {
		PlayerInfo playerInfo = player.playerInfo;
		int fightStrength = calcFight();
		playerInfo.setFightStrength(fightStrength);
		player.syncGuildMemInfo(PlayerSynchType.FIGHT_STRENGTH, fightStrength);
		int hp = properties[FightAttributeType.HP].getTotalJoin();
		int mp = properties[FightAttributeType.MP].getTotalJoin();
		playerInfo.setCurrHP(hp);
		playerInfo.setMaxHP(hp);
		playerInfo.setCurrMP(mp);
		playerInfo.setMaxMP(mp);
		player.sendPacket(Protocol.S_C_PLAYER_ATTRIBUTE, writeFightAttributeMsg());
		player.sendPlayerInfo(false);
	}
}
