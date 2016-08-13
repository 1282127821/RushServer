package com.star.light.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.star.light.guild.GuildMgr;
import com.star.light.prop.BagType;
import com.star.light.prop.Prop;
import com.star.light.prop.PropInstance;
import com.star.light.protocol.Protocol;
import com.star.light.skill.FightSkillMgr;
import com.star.light.skill.FightSkillUnit;

import tbgame.pbmessage.GamePBMsg.ItemInfoMsg;
import tbgame.pbmessage.GamePBMsg.PlayerDetailMsg;
import tbgame.pbmessage.GamePBMsg.PlayerMsg;
import tbgame.pbmessage.GamePBMsg.SkillInfoMsg;
import tbgame.pbmessage.GamePBMsg.SkillMsg;

public final class PlayerMgr {
	private static ConcurrentHashMap<Long, PlayerDetailMsg.Builder> dataMap = new ConcurrentHashMap<Long, PlayerDetailMsg.Builder>();

	public static void queryPlayerDetailMsg(GamePlayer player, long otherUserId) throws Exception {
		PlayerDetailMsg.Builder detailMsg = PlayerDetailMsg.newBuilder();
		GamePlayer otherPlayer = WorldMgr.getOnlinePlayer(otherUserId);
		if (otherPlayer != null && otherPlayer.isOnline()) {
			buildOnlinePlayerDetail(detailMsg, otherPlayer, true);
			// 在线玩家移除临时保存的信息
			if (dataMap.containsKey(otherPlayer.getUserId())) {
				dataMap.remove(otherPlayer.getUserId());
			}
		} else {
			// 如果已经存在则保存
			if (dataMap.containsKey(otherUserId)) {
				detailMsg = dataMap.get(otherUserId);
			} else {
				buildOfflinePlayerDetail(detailMsg, otherUserId, true);
				// 离线玩家保存玩家详细信息
				dataMap.put(otherUserId, detailMsg);
			}
		}

		player.sendPacket(Protocol.S_C_PLAYER_DETAIL_INFO, detailMsg);
	}

	public static void clearData() {
		dataMap.clear();
	}

	public static void removeDataByUserId(long userId) {
		dataMap.remove(userId);
	}

	/**
	 * 构建在线玩家详细信息
	 */
	public static void buildOnlinePlayerDetail(PlayerDetailMsg.Builder detailMsg, GamePlayer player, boolean isNotPK) {
		// 玩家信息
		PlayerMsg.Builder playerMsg = buildPlayerInfoMsg(player.playerInfo);
		if (isNotPK) { // 装备信息 这个是只在查询玩家离线信息的时候需要带上，其它情况应该不用，比如PK、组队副本等
			Prop[] equips = player.getPropMgr().getAllProp(BagType.EQUIP_FENCE);
			for (Prop equip : equips) {
				if (equip != null) {
					ItemInfoMsg.Builder itemInfoMsg = ItemInfoMsg.newBuilder();
					Prop.writePropInfo(equip.getPropInstance(), itemInfoMsg);
					detailMsg.addItemInfoList(itemInfoMsg);
				}
			}
		} else {
			FightSkillMgr fightSkillInventory = player.getSkillInventory();
			SkillMsg.Builder skillMsg = SkillMsg.newBuilder();
			fightSkillInventory.buildWrite(skillMsg);
			detailMsg.setSkillInfo(skillMsg);
		}
		detailMsg.setPlayerInfo(playerMsg);
	}

	/**
	 * 构建玩家离线详细信息
	 */
	public static void buildOfflinePlayerDetail(PlayerDetailMsg.Builder detailMsg, long userId, boolean isNotPK) {
		PlayerInfoDao playerInfoDao = DaoMgr.playerInfoDao;
		PlayerInfo playerInfo = playerInfoDao.getPlayerInfo(userId);
		// 玩家信息
		PlayerMsg.Builder playerMsg = buildPlayerInfoMsg(playerInfo);
		int jobId = playerInfo.getJobId();
		if (isNotPK) {
			// 装备信息
			List<PropInstance> equipProps = DaoMgr.itemInfoDao.getTotalEquipInfo(userId);
			for (PropInstance propInstance : equipProps) {
				ItemInfoMsg.Builder itemInfoMsg = ItemInfoMsg.newBuilder();
				Prop.writePropInfo(propInstance, itemInfoMsg);
				detailMsg.addItemInfoList(itemInfoMsg);
			}
		} else {
			List<FightSkillUnit> playerSkills = new ArrayList<FightSkillUnit>();
			DaoMgr.fightSkillInfoDao.getFightSkillInfo(userId, jobId, playerSkills);
			SkillMsg.Builder skillMsg = SkillMsg.newBuilder();
			for (FightSkillUnit info : playerSkills) {
				SkillInfoMsg.Builder infoMsg = SkillInfoMsg.newBuilder();
				infoMsg.setSkillId(info.getSkillId());
				infoMsg.setSkillLv(info.getSkillLv());
				skillMsg.addSkillInfoList(infoMsg);
			}
			detailMsg.setSkillInfo(skillMsg);
		}
		detailMsg.setPlayerInfo(playerMsg);
	}

	public static PlayerMsg.Builder buildPlayerInfoMsg(PlayerInfo playerInfo) {
		PlayerMsg.Builder playerMsg = PlayerMsg.newBuilder();
		playerMsg.setUserId(playerInfo.getUserId());
		playerMsg.setUserName(playerInfo.getUserName());
		playerMsg.setJobId(playerInfo.getJobId());
		playerMsg.setPlayerLv(playerInfo.getPlayerLv());
		playerMsg.setFightStrength(playerInfo.getFightStrength());
		playerMsg.setPlayerExp(playerInfo.getPlayerExp());
		playerMsg.setPosX(playerInfo.getPosX());
		playerMsg.setPosY(playerInfo.getPosY());
		playerMsg.setDirection(playerInfo.getDirect());
		long guildId = playerInfo.getGuildId();
		if (guildId > 0) {
			playerMsg.setGuildId(playerInfo.getGuildId());
			playerMsg.setGuildName(GuildMgr.getInstance().getGuildNameById(guildId));
		}
		playerMsg.setCurrHP(playerInfo.getCurrHP());
		playerMsg.setMaxHP(playerInfo.getMaxHP());
		return playerMsg;
	}
}