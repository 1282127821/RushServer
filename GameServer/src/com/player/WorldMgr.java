package com.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.guild.GuildMgr;
import com.netmsg.PBMessage;
import com.netmsg.player.GamePlayerDisposeCmd;
import com.util.GameLog;
import com.util.StringUtil;
import com.util.TimeUtil;

public class WorldMgr {
	private static final int INTERVALTIME = 900; // 15分钟 = 900秒

	/**
	 * 在线人数
	 */
	public static AtomicInteger onlineCount = new AtomicInteger(0);
	private static Players players = new Players();

	/**
	 * 获取一个用户，不管用户在不在线，包括从数据库中获取一个玩家，使用的时候应该注意需求
	 */
	public static GamePlayer getPlayer(long userId) {
		if (userId <= 0) {
			GameLog.error("获取用户数据不存在: " + userId);
			return null;
		}

		GamePlayer gamePlayer = players.getPlayerById(userId);
		if (gamePlayer != null) {
			return gamePlayer;
		}

		// 从数据库中加载用户信息
		PlayerInfo playerInfo = DaoMgr.playerInfoDao.getPlayerInfo(userId);
		if (playerInfo == null) {
			GameLog.error("当前数据库playerinfo中不存在UserId: " + userId);
			return null;
		}

		// 从数据库中加载共享信息
		GamePlayer player = new GamePlayer();
		if (!player.loadShareData(playerInfo)) {
			GameLog.error("实例化用户 共享数据失败，请检查数据库初始化。 UserId: " + userId);
			return null;
		}
		players.addPlayer(userId, player);
		return player;
	}

	/**
	 * 根据玩家Id获取在线玩家列表里面的某个玩家,只针对在线的玩家
	 */
	public static GamePlayer getOnlinePlayer(long userId) {
		return players.getPlayerById(userId);
	}

	/**
	 * 根据名字获取在线玩家列表里面的某个玩家,只针对在线的玩家
	 */
	public static GamePlayer getPlayerByName(String charName) {
		return players.getPlayerByName(charName);
	}

	/**
	 * 用户是否存在
	 */
	public static boolean isExist(long userId) {
		return players.isExist(userId);
	}

	/**
	 * 定时保存
	 */
	public static void save() {
		saveUserData();
		GuildMgr.getInstance().saveAllGuild();
	}

	/**
	 * 保存玩家数据
	 */
	public static void saveUserData() {
		long userId = 0;
		try {
			List<PlayerData> playerList = players.datas();
			for (PlayerData playerData : playerList) {
				GamePlayer player = playerData.player;
				userId = player.getUserId();
				player.save();
				// 离线玩家的数据需要被卸载
				if (TimeUtil.getSysCurSeconds() - playerData.updateTime > INTERVALTIME) {
					disposePlayer(player);
				}
			}
		} catch (Exception e) {
			GameLog.error("保存数据出错,userId: " + userId, e);
		}
	}

	public static void disposePlayer(GamePlayer player) {
		PBMessage pkg = new PBMessage((short) 0, player.getUserId());
		player.enqueue(new GamePlayerDisposeCmd(), pkg);
	}

	public static void unLoadPlayer(GamePlayer player) {
		players.remove(player.getUserId());
		player.unLoadShareData();
	}

	/**
	 * 玩家退出
	 */
	public static boolean logout(GamePlayer player) {
		if (player == null || !player.isOnline()) {
			return false;
		}

		try {
			player.unLoadPersonData();
			PlayerMgr.removeDataByUserId(player.getUserId());
		} catch (Exception ex) {
			GameLog.error("用户退出失败, userId : " + player.getUserId() + ", userName : " + player.getUserName() + ", state : " + player.getPlayerState(), ex);
		}
		return true;
	}

	/**
	 * 获取全部用户
	 */
	public static List<GamePlayer> getAllPlayers() {
		return players == null ? new ArrayList<GamePlayer>() : players.values();
	}

	public static int getOnlineCount() {
		return WorldMgr.onlineCount.get();
	}

	public static List<GamePlayer> getOnlineList() {
		List<GamePlayer> playerList = new ArrayList<GamePlayer>();
		for (GamePlayer player : players.values()) {
			if (player.isOnline()) {
				playerList.add(player);
			}
		}
		return playerList;
	}

	/**
	 * 凌晨5点重置在线玩家的数据
	 */
	public static void resetData() {
		try {
			Date currentDate = new Date();
			List<GamePlayer> gamePlayerList = players.values();
			for (GamePlayer gamePlayer : gamePlayerList) {
				if (gamePlayer != null && gamePlayer.isOnline()) {
					gamePlayer.refershData(currentDate);
				}
			}
		} catch (Exception e) {
			GameLog.error("凌晨5点重置在线玩家的数据错误", e);
		}
	}
}

class Players {
	private ConcurrentHashMap<Long, PlayerData> playerContext = new ConcurrentHashMap<Long, PlayerData>();

	/**
	 * 根据玩家的Id获得该玩家
	 */
	public GamePlayer getPlayerById(long userId) {
		PlayerData playerData = playerContext.get(userId);
		if (playerData != null) {
			playerData.updateTime = TimeUtil.getSysCurSeconds();
			return playerData.player;
		}

		return null;
	}

	/**
	 * 根据玩家名字获得该玩家
	 */
	public GamePlayer getPlayerByName(String charName) {
		if (StringUtil.isNotNullOrEmpty(charName)) {
			for (PlayerData info : playerContext.values()) {
				if (info != null) {
					GamePlayer gamePlayer = info.player;
					if (gamePlayer != null && gamePlayer.getUserName().equals(charName)) {
						return gamePlayer;
					}
				}
			}
		}

		return null;
	}

	/**
	 * 是否存在
	 */
	public boolean isExist(long userId) {
		return playerContext.containsKey(userId);
	}

	/**
	 * 添加一个用户
	 */
	public void addPlayer(long userId, GamePlayer player) {
		if (!playerContext.containsKey(userId)) {
			playerContext.put(userId, new PlayerData(player));
		}
	}

	/**
	 * 移除一个用户
	 */
	public void remove(long userId) {
		playerContext.remove(userId);
	}

	public void clear() {
		playerContext.clear();
	}

	public List<GamePlayer> values() {
		List<GamePlayer> infos = new ArrayList<GamePlayer>();
		for (PlayerData info : playerContext.values()) {
			if (info != null && info.player != null) {
				infos.add(info.player);
			}
		}

		return infos;
	}

	public List<PlayerData> datas() {
		List<PlayerData> infos = new ArrayList<PlayerData>();
		infos.addAll(playerContext.values());
		return infos;
	}
}

class PlayerData {
	public int updateTime;
	public GamePlayer player;

	public PlayerData(GamePlayer player) {
		updateTime = TimeUtil.getSysCurSeconds();
		this.player = player;
	}
}
